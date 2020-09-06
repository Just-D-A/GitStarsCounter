package com.example.gitstarscounter.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class TimerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        Log.d("TimerReceiver", "onReceive " + intent.action)
        context?.startService(Intent(context, StarIntentService::class.java))
    }
}
