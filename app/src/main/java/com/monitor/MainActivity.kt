package com.monitor

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        update.setOnClickListener {
            sendNotification()
        }
    }

    fun getBatteryLevel(): Float {
        val batteryIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        return if (level == -1 || scale == -1) {
            50.0f
        } else
            level.toFloat() / scale.toFloat() * 100.0f
    }

    fun sendNotification() {
        val channelId = "chenne_id"
        val id = 1
        val icon = android.R.drawable.ic_dialog_info
        val level = getBatteryLevel().toInt()
        val title = "A bateria está em $level%"

        val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentText("100%")
                .setProgress(100, level, false)
                .setAutoCancel(true)
        createNotificationChannel("name channel", channelId)
        with(NotificationManagerCompat.from(this)) {
            cancel(id)
            notify(id, builder.build())
        }
    }

    private fun createNotificationChannel(name: String, CHANNEL_ID: String ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val descriptionText = "Descricao notification"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
