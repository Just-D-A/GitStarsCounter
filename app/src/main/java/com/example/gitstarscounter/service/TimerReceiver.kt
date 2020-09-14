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
        //val serviceIntent = Intent(context, StarIntentService::class.java)//foreground
        val serviceIntent = Intent(context, StarJobIntentService::class.java)//job
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //    Log.d("TimerReciver", "StartIntentService")
            // context.startForegroundService(serviceIntent)
            /*     JobIntentService.enqueueWork(context,
                     StarJobIntentService::class.java,
                     1, serviceIntent    )*/
            //context.startService(serviceIntent)
            StarJobIntentService.enqueueWork(context, Intent())
        } else {
            Log.d("TimerReciver", "Anouther service")
        }
        Log.d("TimerReceiverEND", "onReceive " + intent.action)
    }
}
