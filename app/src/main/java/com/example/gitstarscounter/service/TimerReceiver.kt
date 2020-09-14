package com.example.gitstarscounter.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService

class TimerReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TimerReceiver", "onReceive " + intent.action)
        val serviceIntent = Intent(context, StarIntentService::class.java)//foreground

        //    Log.d("TimerReciver", "StartIntentService")
        context.startForegroundService(serviceIntent)
        //checking

//        StarJobIntentService.enqueueWork(context, Intent())

        Log.d("TimerReceiverEND", "onReceive " + intent.action)
    }
}
