import 'dart:async';
import 'dart:io';

import 'package:finger_print_pad/finger_print_pad.dart';
import 'package:flutter/material.dart';

void main() {
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
  final String _platformVersion = 'Unknown';
  final _fingerPrintPadPlugin = FingerPrintPad();

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
        body: Column(
          children: [
            Text('Running on: $_platformVersion\n'),
            imageFile == null
                ? const Text('No image selected.')
                : Image.file(imageFile!),
            init == true ? const Text('init') : const Text('not init'),
            ElevatedButton(
              onPressed: () async {
                var init = await _fingerPrintPadPlugin.openDevice();
                setState(() {
                  init = init;
                });
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
                if (image != null) {
                  setState(
                    () {
                      imageFile = File.fromRawPath(image);
                    },
                  );
                }
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
