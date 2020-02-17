package com.monitor

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


class MonitorService: IntentService("MonitorService") {
    private val timer = Timer()

    override fun onHandleIntent(intent: Intent?) {
        val period = MainActivity.intervalInMinutes
        val timerTask = object : TimerTask() {
            override fun run() {
                sendNotification()
            }
        }
        timer.schedule(timerTask, 1, period)
    }

    private fun getBatteryLevel(): Float {
        val batteryIntent = registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
        val level = batteryIntent!!.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)

        return if (level == -1 || scale == -1) {
            50.0f
        } else
            level.toFloat() / scale.toFloat() * 100.0f
    }

    private fun sendNotification() {
        val channelId = "chenne_id"
        val id = 1
        val icon = android.R.drawable.ic_dialog_info
        val level = getBatteryLevel().toInt()
        val title = "A bateria está em $level%"

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(icon)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setProgress(100, level, false)
            .setAutoCancel(true)
        createNotificationChannel("name channel", channelId)
        with(NotificationManagerCompat.from(this)) {
            cancel(id)
            notify(id, builder.build())
        }

        println("Notificacao com level $level")
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

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}