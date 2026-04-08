import 'package:flutter_test/flutter_test.dart';

import 'package:bbtotal/main.dart';

void main() {
  testWidgets('hybrid shell home renders', (WidgetTester tester) async {
    await tester.pumpWidget(const BbtotalApp());

    expect(find.text('Hybrid 容器 MVP'), findsOneWidget);
    expect(find.text('状态'), findsOneWidget);
    expect(find.text('目标页面'), findsOneWidget);
    expect(find.text('打开内嵌页'), findsOneWidget);
  });
}
