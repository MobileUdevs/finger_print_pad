// import 'package:flutter_test/flutter_test.dart';
// import 'package:finger_print_pad/finger_print_pad.dart';
// import 'package:finger_print_pad/finger_print_pad_platform_interface.dart';
// import 'package:finger_print_pad/finger_print_pad_method_channel.dart';
// import 'package:plugin_platform_interface/plugin_platform_interface.dart';
//
// class MockFingerPrintPadPlatform
//     with MockPlatformInterfaceMixin
//     implements FingerPrintPadPlatform {
//
//   @override
//   Future<String?> getPlatformVersion() => Future.value('42');
// }
//
// void main() {
//   final FingerPrintPadPlatform initialPlatform = FingerPrintPadPlatform.instance;
//
//   test('$MethodChannelFingerPrintPad is the default instance', () {
//     expect(initialPlatform, isInstanceOf<MethodChannelFingerPrintPad>());
//   });
//
//   test('getPlatformVersion', () async {
//     FingerPrintPad fingerPrintPadPlugin = FingerPrintPad();
//     MockFingerPrintPadPlatform fakePlatform = MockFingerPrintPadPlatform();
//     FingerPrintPadPlatform.instance = fakePlatform;
//
//     expect(await fingerPrintPadPlugin.getPlatformVersion(), '42');
//   });
// }
