package com.rabbitimpulsorademercados.sensorproximidad

import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PowerManager
import android.view.WindowManager

class MainActivity : AppCompatActivity() {

    private var sensorManager: SensorManager? = null
    private var mProximitySensor: Sensor? = null


    //Screen ON
    //val power = applicationContext.getSystemService(POWER_SERVICE) as PowerManager
    //var isScreenOn: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, Service::class.java)

        startService(intent)

        sensorManager = applicationContext.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        mProximitySensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_PROXIMITY)
        if (mProximitySensor == null) {
            //proximitySensor.text = "No Proximity Sensor!"
        } else {
            sensorManager!!.registerListener(proximitySensorEventListener, mProximitySensor, SensorManager.SENSOR_DELAY_NORMAL)

        }


    }
        var proximitySensorEventListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {

            }

            override fun onSensorChanged(event: SensorEvent) {
                val params = this@MainActivity.window.attributes
                if (event.sensor.type == Sensor.TYPE_PROXIMITY) {

                    if (event.values[0] == 0f) {
                        params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        //params.screenBrightness = 0f
                        window.attributes = params

                    } else {
                        params.flags = params.flags or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        //params.screenBrightness = -1f
                        window.attributes = params

                    }
                }

            }
        }



}




