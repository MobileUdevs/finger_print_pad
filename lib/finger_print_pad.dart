import 'package:flutter/services.dart';

import 'finger_print_pad_platform_interface.dart';

final class FingerPrintPad {
  FingerPrintPad._();

  static FingerPrintPad get instance => FingerPrintPad._();

  Future<void> init() {
    return FingerPrintPadPlatform.instance.init();
  }

  Future<void> openDevice() {
    return FingerPrintPadPlatform.instance.openDevice();
  }

  Future<void> closeDevice() {
    return FingerPrintPadPlatform.instance.closeDevice();
  }

  Future<String?> captureAndSaveFinger() {
    return FingerPrintPadPlatform.instance.captureAndSaveFinger();
  }

  Future<void> compareFinger() {
    return FingerPrintPadPlatform.instance.compareFinger();
  }
}
