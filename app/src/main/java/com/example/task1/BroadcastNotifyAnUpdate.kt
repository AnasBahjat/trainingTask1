package com.example.task1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastNotifyAnUpdate(private val listener : BroadcastReceiverListener): BroadcastReceiver() {
    interface BroadcastReceiverListener{
        fun onBroadcastReceived(id : Int)
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == Constants.DELETE_MOVIE_ACTION){
            val id = intent.getIntExtra(Constants.ID_TO_SAVE,-5)
            listener.onBroadcastReceived(id)
        }
    }
}