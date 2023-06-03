import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:finger_print_pad/finger_print_pad_method_channel.dart';

void main() {
  TestWidgetsFlutterBinding.ensureInitialized();

  MethodChannelFingerPrintPad platform = MethodChannelFingerPrintPad();
  const MethodChannel channel = MethodChannel('finger_print_pad');

  setUp(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(
      channel,
      (MethodCall methodCall) async {
        return '42';
      },
    );
  });

  tearDown(() {
    TestDefaultBinaryMessengerBinding.instance.defaultBinaryMessenger.setMockMethodCallHandler(channel, null);
  });

  test('getPlatformVersion', () async {
    expect(await platform.getPlatformVersion(), '42');
  });
}
