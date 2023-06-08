import 'dart:async';
import 'dart:io';
import 'dart:typed_data';

import 'package:finger_print_pad/finger_print_pad.dart';
import 'package:flutter/material.dart';

void main() {
  FingerPrintPad.instance.init();
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({super.key});

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  File? imageFile;
  bool init = false;
  String? fingerPrint;
  final String _platformVersion = 'Unknown';
  final _fingerPrintPadPlugin = FingerPrintPad.instance;

  @override
  void initState() {
    super.initState();
    // initPlatformState();
  }

  @override
  didChangeDependencies() {
    super.didChangeDependencies();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    await _fingerPrintPadPlugin.init();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: ListView(
          children: [
            Text('Running on: $fingerPrint'),
            imageFile == null
                ? const Text('No image selected.')
                : Image.file(imageFile!),
            init == true ? const Text('init') : const Text('not init'),
            ElevatedButton(
              onPressed: () async {
                await _fingerPrintPadPlugin.openDevice();
              },
              child: const Text('openDevice'),
            ),
            ElevatedButton(
              onPressed: () async {
                await _fingerPrintPadPlugin.closeDevice();
              },
              child: const Text('closeDevice'),
            ),
            ElevatedButton(
              onPressed: () async {
                final image =
                    await _fingerPrintPadPlugin.captureAndSaveFinger();
                // if (image == null) return;
                setState(
                  () {
                    fingerPrint = image;
                    // imageFile = File.fromRawPath(image);
                  },
                );
              },
              child: const Text('captureAndSaveFinger'),
            ),
            ElevatedButton(
              onPressed: () async {
                await _fingerPrintPadPlugin.compareFinger();
              },
              child: const Text('compareFinger'),
            ),
          ],
        ),
      ),
    );
  }
}
