import 'dart:async';
import 'dart:io';
import 'dart:typed_data';

import 'package:finger_print_pad/finger_model.dart';
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
            SafeArea(
              minimum: const EdgeInsets.all(16.0),
              child: ElevatedButton(
                onPressed: () async {
                  await _fingerPrintPadPlugin.scanFinger(
                    FingerModel(
                      token: "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOi8vdGVzdC1uYXpvcmF0LmthZGFzdHIudXovYXBpL2F1dGgvbG9naW4iLCJpYXQiOjE2ODY2Njc2MDQsImV4cCI6MTY4NjY3NDgwNCwibmJmIjoxNjg2NjY3NjA0LCJqdGkiOiJrT2V3S0NsWkd3NjVyNWpYIiwic3ViIjoiMTY2NiIsInBydiI6IjIzYmQ1Yzg5NDlmNjAwYWRiMzllNzAxYzQwMDg3MmRiN2E1OTc2ZjcifQ.MI1w4-lRhMUc_d4ULdHA_LF8tjZJhXBkUZBh6bCiC0E",
                      key: 'fingerprint',
                      taskId: "59869",
                      baseUrl: "https://test-nazorat.kadastr.uz/",
                    ),
                  ).then((value) {

                  });
                },
                child: const Text('Scan Finger'),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
