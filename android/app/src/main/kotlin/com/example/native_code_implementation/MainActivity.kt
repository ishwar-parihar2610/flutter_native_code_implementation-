package com.example.native_code_implementation

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val METHOD_CHANNEL_NAME = "/method"
    private val PRESSURE_CHANNEL_NAME = "/pressure"
    private var methodChannel: MethodChannel? = null
    private lateinit var sensorManager: SensorManager
    private var pressureChannel: EventChannel? = null
    private var pressureStreamHandler: StreamHandler? = null

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        setupChannels(this, flutterEngine.dartExecutor.binaryMessenger)
    }

    override fun onDestroy() {
        teardownChannels();
        super.onDestroy()
    }


    private fun setupChannels(context: Context, messenger: BinaryMessenger) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        methodChannel = MethodChannel(messenger, METHOD_CHANNEL_NAME)
        methodChannel!!.setMethodCallHandler { call, result ->
            if (call.method == "isSensorAvailable") {
                Intent(getContext(),MyService::class.java).also {
                    it.putExtra("EXTRA_DATA","My name is ishwar")
                    startService(it)
                }
                result.success(sensorManager!!.getSensorList(Sensor.TYPE_PRESSURE).isNotEmpty())
            } else {
                result.notImplemented()
            }
           var value :String ="Hy i am ishwar";
            methodChannel!!.invokeMethod("toast", value).run {

            }


        }

        pressureChannel = EventChannel(messenger, PRESSURE_CHANNEL_NAME)
        pressureStreamHandler = StreamHandler(sensorManager!!, Sensor.TYPE_PRESSURE)
        pressureChannel!!.setStreamHandler(pressureStreamHandler)


    }

    private fun teardownChannels() {
        methodChannel!!.setMethodCallHandler(null)
        pressureChannel!!.setStreamHandler(null)
    }
}
