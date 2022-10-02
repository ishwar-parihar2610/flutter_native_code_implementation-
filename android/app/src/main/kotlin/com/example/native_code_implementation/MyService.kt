package com.example.native_code_implementation

import android.app.Service
import android.content.Intent
import android.hardware.SensorManager

import android.os.IBinder
import android.util.Log
import android.widget.Toast


class MyService: Service(){
    private lateinit var sensorManager: SensorManager
    val Tag="My SERVICE"
    init {
        Log.d(Tag, ":Service Function Called ")
    }
    override fun onBind(p0: Intent?): IBinder? =null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dataString =intent?.getStringExtra("EXTRA_DATA")
            printFunction()
        dataString?.let{

            Log.d(Tag, "onStartCommand: ${dataString}")
        }
        return  START_STICKY
    }

    fun printFunction(){
        Toast.makeText(baseContext, "service called", Toast.LENGTH_SHORT).show()
        printFunction()
    }
}