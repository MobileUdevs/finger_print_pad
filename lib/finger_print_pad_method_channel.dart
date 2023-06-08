import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'finger_print_pad_platform_interface.dart';

/// An implementation of [FingerPrintPadPlatform] that uses method channels.
class MethodChannelFingerPrintPad extends FingerPrintPadPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('finger_print_pad');

  @override
  Future<String?> getPlatformVersion() async {
    final version =
        await methodChannel.invokeMethod<String>('getPlatformVersion');
    return version;
  }

  // @override
  // Future<void> init() async {
  //   await methodChannel.invokeMethod<void>('init');
  // }
  @override
  Future<void> init() async {
    try {
      await methodChannel.invokeMethod<void>('init');
    } catch (e) {
      throw ('Failed to initialize FingerprintC_FBI: $e');
    }
  }

  @override
  Future<void> openDevice() async {
    await methodChannel.invokeMethod<bool>('openDevice');
  }

  @override
  Future<void> closeDevice() async {
    await methodChannel.invokeMethod<void>('closeDevice');
  }

  @override
  Future<String?> captureAndSaveFinger() async {
    return await methodChannel.invokeMethod<String?>('saveFinger');
  }

  @override
  Future<void> compareFinger() async {
    await methodChannel.invokeMethod<void>('compareFinger');
  }

  @override
  Future<void> scanFinger() async {
    await methodChannel.invokeMethod<void>('scanFinger');
  }
}
