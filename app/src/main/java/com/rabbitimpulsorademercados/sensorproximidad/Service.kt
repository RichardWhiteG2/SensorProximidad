package com.rabbitimpulsorademercados.sensorproximidad

import android.app.Service
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService

class Service : Service() {
    private val TAG = "service"
    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
    private var sensorManager: SensorManager? = null
    private var mProximitySensor: Sensor? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "Start")

        Handler().apply {
            sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
            mProximitySensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
            if (mProximitySensor == null) {
                //proximitySensor.text = "No Proximity Sensor!"
            } else {
                sensorManager!!.registerListener(proximitySensorEventListener, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL)

            }

        }
        //return super.onStartCommand(intent, flags, startId)

        return START_STICKY
    }
    //val power = applicationContext.getSystemService(POWER_SERVICE) as PowerManager
    //val power = Context.POWER_SERVICE as PowerManager
    //var power: PowerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
    //var isScreenOn: Boolean = true
    var proximitySensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

        }

        override fun onSensorChanged(event: SensorEvent) {
            if (event.sensor.type == Sensor.TYPE_PROXIMITY) {
                Log.d(TAG,"Se registra cambio.")
                var isScreenOn: Boolean = true
                if (event.values[0] == 0f) {

                    val power: PowerManager.WakeLock =
                        (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                            //isScreenOn= isInteractive

                            if (Build.VERSION.SDK_INT >= 20) isScreenOn=isInteractive else isScreenOn=isScreenOn
                            if (!isScreenOn) { //¿La pantalla esta apagada?
                            newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, "MyApp::MyWakelockTag").apply {

                                    //La pantalla esta apagada!, se enciende.
                                    Log.d("Sensor","pantalla apagada prende")
                                    acquire()   //PARTIAL_WAKE_LOCK



                            }}else {
                                //La pantalla esta encendida!

                            newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK,"MyApp::MyWakelockTagOff").apply {
                                acquire()
                                Log.d("Sensor","pantalla prendida se apaga")
                            }
                        }}
                }
            }

        }
    }
    override fun onDestroy() {
        Log.d(TAG, "Destroy")
        super.onDestroy()
    }
}