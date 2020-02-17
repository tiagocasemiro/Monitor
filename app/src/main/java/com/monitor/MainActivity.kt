package com.monitor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object {
        var intervalInMinutes = 10 * 60 * 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val service = Intent(MainActivity@this, MonitorService::class.java)

        start.setOnClickListener {
            setInterval()
            startService(service)
        }
        stop.setOnClickListener {
            setInterval()
            stopService(service)
        }
    }

    fun setInterval() {
        try {
            val text = interval.text.toString()
            if(text.isNotEmpty()) {
                intervalInMinutes = text.toLong() * 60 * 1000L
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
