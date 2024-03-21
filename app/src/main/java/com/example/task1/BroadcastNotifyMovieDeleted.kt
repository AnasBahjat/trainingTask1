package com.example.task1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastNotifyMovieDeleted(private val listener : BroadcastReceiverListener): BroadcastReceiver() {
    interface BroadcastReceiverListener{
        fun onBroadcastReceived(id : Int)
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "deleteMovie"){
            val id = intent.getIntExtra("id",-5)
            listener.onBroadcastReceived(id)
        }
    }
}