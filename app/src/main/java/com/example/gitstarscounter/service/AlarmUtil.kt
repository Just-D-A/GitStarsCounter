package com.example.gitstarscounter.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build

class AlarmUtil { // перенести в Entity??
    @SuppressLint("ServiceCast")
    fun setAlarm(cotext: Context) {
        val alarmManager = cotext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentToTimerReceiver = Intent(cotext, TimerReceiver::class.java)
        intentToTimerReceiver.action = ACTION

        val pendingIntent = PendingIntent.getBroadcast(
            cotext,
            0,
            intentToTimerReceiver,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val periodInMiliseconds: Long =
            (PERIOD_IN_MINUTS * SECONDS_IN_MINUT * MILLISECONDS_IN_SECOND).toLong()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                periodInMiliseconds,
                periodInMiliseconds,
                pendingIntent
            )
        } else {
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                periodInMiliseconds,
                periodInMiliseconds,
                pendingIntent
            )
        }
    }

    private fun cancelAlarm(ctx: Context) {
        val am = ctx.applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intentToTimerReceiver = Intent(ctx.applicationContext, TimerReceiver::class.java)
        intentToTimerReceiver.action = ACTION

        val pendingIntent = PendingIntent.getBroadcast(
            ctx.applicationContext,
            0,
            intentToTimerReceiver,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        am.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    companion object {
        const val PERIOD_IN_MINUTS = 0.1
        const val SECONDS_IN_MINUT = 60
        const val MILLISECONDS_IN_SECOND = 1000

        const val ACTION = "StarIntentService"
        const val TAG = "AlarmReceiver"
    }
}
