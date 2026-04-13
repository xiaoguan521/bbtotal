import 'package:bbtotal/models/remote_bridge_bundle.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:bbtotal/main.dart';

void main() {
  testWidgets('hybrid entry home renders', (WidgetTester tester) async {
    await tester.pumpWidget(
      const BbtotalApp(remoteBridgeBundle: RemoteBridgeBundle.embeddedFallback),
    );

    expect(find.text('打卡入口'), findsOneWidget);
    expect(find.text('输入姓名后按回车自动加载'), findsOneWidget);
    expect(find.text('最近 5 次打卡位置'), findsOneWidget);
    expect(find.text('主动发起'), findsOneWidget);
    expect(find.text('待办处理'), findsOneWidget);
  });
}
