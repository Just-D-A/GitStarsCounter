package com.example.gitstarscounter.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log


class Alarm {
    val TAG = "AlarmReceiver"
    fun setAlarm(ctx: Context) {
        val am = ctx.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentToTimerReceiver = Intent(ctx.applicationContext, TimerReceiver::class.java)
        intentToTimerReceiver.action = "getNewStars"
        val pendingIntent = PendingIntent.getBroadcast(
            ctx.applicationContext,
            0,
            intentToTimerReceiver,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val periodInMinutes = 0.5
        val periodInMiliseconds: Long = (periodInMinutes * 60 * 1000).toLong()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            am.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                periodInMiliseconds,
                periodInMiliseconds,
                pendingIntent
            )
        } else {
            am.setRepeating(
                AlarmManager.RTC_WAKEUP,
                periodInMiliseconds,
                periodInMiliseconds,
                pendingIntent
            )
        }
    }

    private fun cancelAlarm(ctx: Context) {
        Log.i(TAG, "cancelAlarm")
        val am = ctx.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentToTimerReceiver = Intent(ctx.applicationContext, TimerReceiver::class.java)
        intentToTimerReceiver.action = "someAction"
        val pendingIntent = PendingIntent.getBroadcast(
            ctx.applicationContext, 0,
            intentToTimerReceiver,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        am.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
