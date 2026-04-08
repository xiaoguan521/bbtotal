from __future__ import annotations

from pathlib import Path

from PIL import Image, ImageDraw, ImageFilter


ROOT = Path(__file__).resolve().parents[1]
MASTER_SIZE = 1024


def rounded_rect_mask(size: int, radius: int) -> Image.Image:
    mask = Image.new("L", (size, size), 0)
    draw = ImageDraw.Draw(mask)
    draw.rounded_rectangle((0, 0, size, size), radius=radius, fill=255)
    return mask


def vertical_gradient(
    size: int,
    top: tuple[int, int, int],
    bottom: tuple[int, int, int],
) -> Image.Image:
    image = Image.new("RGBA", (size, size))
    pixels = image.load()
    for y in range(size):
        t = y / max(size - 1, 1)
        r = int(top[0] * (1 - t) + bottom[0] * t)
        g = int(top[1] * (1 - t) + bottom[1] * t)
        b = int(top[2] * (1 - t) + bottom[2] * t)
        for x in range(size):
            pixels[x, y] = (r, g, b, 255)
    return image


def draw_icon(size: int = MASTER_SIZE) -> Image.Image:
    canvas = Image.new("RGBA", (size, size), (0, 0, 0, 0))

    # Background
    background = vertical_gradient(
        size=size,
        top=(82, 165, 255),
        bottom=(15, 98, 226),
    )
    glow = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    glow_draw = ImageDraw.Draw(glow)
    glow_draw.ellipse(
        (size * 0.18, size * 0.08, size * 0.9, size * 0.78),
        fill=(164, 224, 255, 90),
    )
    glow = glow.filter(ImageFilter.GaussianBlur(radius=size * 0.05))
    background.alpha_composite(glow)

    draw = ImageDraw.Draw(background)

    # Inner workflow tile
    card_left = int(size * 0.2)
    card_top = int(size * 0.18)
    card_right = int(size * 0.8)
    card_bottom = int(size * 0.8)
    shadow = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    shadow_draw = ImageDraw.Draw(shadow)
    shadow_draw.rounded_rectangle(
        (
            card_left + int(size * 0.02),
            card_top + int(size * 0.03),
            card_right + int(size * 0.02),
            card_bottom + int(size * 0.03),
        ),
        radius=int(size * 0.11),
        fill=(9, 55, 127, 70),
    )
    shadow = shadow.filter(ImageFilter.GaussianBlur(radius=size * 0.03))
    background.alpha_composite(shadow)

    draw.rounded_rectangle(
        (card_left, card_top, card_right, card_bottom),
        radius=int(size * 0.11),
        fill=(247, 251, 255, 255),
    )

    # Flow lines
    line_color = (177, 203, 241, 255)
    draw.rounded_rectangle(
        (
            int(size * 0.29),
            int(size * 0.3),
            int(size * 0.68),
            int(size * 0.34),
        ),
        radius=int(size * 0.02),
        fill=line_color,
    )
    draw.rounded_rectangle(
        (
            int(size * 0.29),
            int(size * 0.39),
            int(size * 0.58),
            int(size * 0.43),
        ),
        radius=int(size * 0.02),
        fill=line_color,
    )

    # Location pin / workflow symbol
    pin_layer = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    pin_draw = ImageDraw.Draw(pin_layer)
    pin_color = (16, 103, 236, 255)
    pin_draw.ellipse(
        (
            int(size * 0.33),
            int(size * 0.43),
            int(size * 0.67),
            int(size * 0.77),
        ),
        outline=pin_color,
        width=int(size * 0.038),
    )
    pin_draw.ellipse(
        (
            int(size * 0.43),
            int(size * 0.53),
            int(size * 0.57),
            int(size * 0.67),
        ),
        fill=pin_color,
    )
    pin_draw.polygon(
        [
            (int(size * 0.5), int(size * 0.86)),
            (int(size * 0.38), int(size * 0.66)),
            (int(size * 0.62), int(size * 0.66)),
        ],
        fill=pin_color,
    )
    background.alpha_composite(pin_layer)

    # Check badge
    badge_bounds = (
        int(size * 0.59),
        int(size * 0.58),
        int(size * 0.82),
        int(size * 0.81),
    )
    draw.ellipse(badge_bounds, fill=(48, 201, 123, 255))
    check = Image.new("RGBA", (size, size), (0, 0, 0, 0))
    check_draw = ImageDraw.Draw(check)
    check_draw.line(
        [
            (int(size * 0.645), int(size * 0.69)),
            (int(size * 0.695), int(size * 0.745)),
            (int(size * 0.775), int(size * 0.645)),
        ],
        fill=(255, 255, 255, 255),
        width=int(size * 0.04),
        joint="curve",
    )
    background.alpha_composite(check)

    # Apply rounded square mask
    mask = rounded_rect_mask(size, radius=int(size * 0.24))
    canvas.alpha_composite(background)
    canvas.putalpha(mask)
    return canvas


def save_png(image: Image.Image, path: Path, size: int) -> None:
    resized = image.resize((size, size), Image.Resampling.LANCZOS)
    path.parent.mkdir(parents=True, exist_ok=True)
    resized.save(path, format="PNG")


def generate() -> None:
    master = draw_icon()

    branding_dir = ROOT / "assets" / "branding"
    branding_dir.mkdir(parents=True, exist_ok=True)
    master_path = branding_dir / "app_icon_master.png"
    master.save(master_path, format="PNG")
    splash_mark = master.resize((320, 320), Image.Resampling.LANCZOS)
    splash_mark_path = branding_dir / "launch_mark.png"
    splash_mark.save(splash_mark_path, format="PNG")

    android_sizes = {
        "mipmap-mdpi": 48,
        "mipmap-hdpi": 72,
        "mipmap-xhdpi": 96,
        "mipmap-xxhdpi": 144,
        "mipmap-xxxhdpi": 192,
    }
    for folder, size in android_sizes.items():
        save_png(master, ROOT / "android" / "app" / "src" / "main" / "res" / folder / "ic_launcher.png", size)

    save_png(
        splash_mark,
        ROOT / "android" / "app" / "src" / "main" / "res" / "drawable-nodpi" / "launch_brand.png",
        320,
    )

    save_png(master, ROOT / "web" / "favicon.png", 32)
    save_png(master, ROOT / "web" / "icons" / "Icon-192.png", 192)
    save_png(master, ROOT / "web" / "icons" / "Icon-512.png", 512)
    save_png(master, ROOT / "web" / "icons" / "Icon-maskable-192.png", 192)
    save_png(master, ROOT / "web" / "icons" / "Icon-maskable-512.png", 512)

    ico_sizes = [(16, 16), (24, 24), (32, 32), (48, 48), (64, 64), (128, 128), (256, 256)]
    ico_path = ROOT / "windows" / "runner" / "resources" / "app_icon.ico"
    ico_path.parent.mkdir(parents=True, exist_ok=True)
    master.save(ico_path, format="ICO", sizes=ico_sizes)


if __name__ == "__main__":
    generate()
