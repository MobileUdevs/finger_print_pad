import 'dart:convert';

import 'package:finger_print_pad/finger_model.dart';
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

  Future<String?> scanFinger(FingerModel fingerModel) {
    print("fingerModel: ${fingerModel.toJson()}");
    return FingerPrintPadPlatform.instance.scanFinger(
      model: jsonEncode(fingerModel.toJson()),
    );
  }
}
