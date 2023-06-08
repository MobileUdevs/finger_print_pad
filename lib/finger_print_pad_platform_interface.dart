import 'package:flutter/services.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'finger_print_pad_method_channel.dart';

abstract class FingerPrintPadPlatform extends PlatformInterface {
  /// Constructs a FingerPrintPadPlatform.
  FingerPrintPadPlatform() : super(token: _token);

  static final Object _token = Object();

  static FingerPrintPadPlatform _instance = MethodChannelFingerPrintPad();

  /// The default instance of [FingerPrintPadPlatform] to use.
  ///
  /// Defaults to [MethodChannelFingerPrintPad].
  static FingerPrintPadPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [FingerPrintPadPlatform] when
  /// they register themselves.
  static set instance(FingerPrintPadPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> init() {
    throw UnimplementedError('init() has not been implemented.');
  }

  Future<void> openDevice() {
    throw UnimplementedError('openDevice has not been implemented.');
  }

  Future<void> closeDevice() {
    throw UnimplementedError('closeDevice has not been implemented.');
  }

  Future<String?> captureAndSaveFinger() {
    throw UnimplementedError('captureAndSaveFinger has not been implemented.');
  }

  Future<void> compareFinger() {
    throw UnimplementedError('CompareFinger has not been implemented.');
  }
}
