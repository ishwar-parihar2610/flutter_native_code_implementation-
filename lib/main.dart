import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key}) : super(key: key);

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  static const methodChannel = MethodChannel('/method');
  static const pressureChannel = EventChannel('/pressure');

  String sensorAvailable = "unknown";
  double pressureReading = 0;
  StreamSubscription? pressureSubscription;

  Future<void> _checkAvailability() async {
    try {
      var available = await methodChannel.invokeMethod('isSensorAvailable');
      setState(() {
        sensorAvailable = available.toString();
      });
    } catch (e) {
      print(e);
    }

    methodChannel.setMethodCallHandler((call) async {
      if (call.method == "toast") {
      String value=call.arguments;
        Fluttertoast.showToast(msg: "${call.arguments.toString()}");
      }
    });
  }

  _startReading() {
    print("call function");
    pressureSubscription =
        pressureChannel.receiveBroadcastStream().listen((event) {
      setState(() {
        print("event is ${event}");
        pressureReading = event;
      });
    });
  }

  _stopReading() {
    setState(() {
      pressureReading = 0;
    });
    pressureSubscription?.cancel();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.center,
          children: [
            Text("Sensor Available ? : $sensorAvailable"),
            ElevatedButton(
                onPressed: () => _checkAvailability(),
                child: const Text("Check Sensor Available")),
            SizedBox(
              height: 50.0,
            ),
            if (pressureReading != 0)
              Text('Sensor Reading: ${pressureReading}'),
            if (sensorAvailable == 'true' && pressureReading == 0)
              ElevatedButton(
                  onPressed: () {
                    _startReading();
                  },
                  child: Text("Start Reading")),
            if (pressureReading != 0)
              ElevatedButton(
                  onPressed: () => _stopReading, child: Text("Stop Reading")),
            SizedBox(
              height: 10,
            ),
            ElevatedButton(onPressed: () {}, child: Text("Call")),
          ],
        ),
      ),
    );
  }
}
