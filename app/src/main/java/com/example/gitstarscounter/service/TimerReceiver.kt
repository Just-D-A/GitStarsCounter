package com.example.gitstarscounter.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

class TimerReceiver : BroadcastReceiver() {
    //  @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("TimerReceiver", "onReceive " + intent.action)
        val serviceIntent = Intent(context, StarIntentService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
          context.startService(serviceIntent)
        } else {
            Log.d("TimerReciver", "Anouther service")
        }
        Log.d("TimerReceiverEND", "onReceive " + intent.action)
    }
}
