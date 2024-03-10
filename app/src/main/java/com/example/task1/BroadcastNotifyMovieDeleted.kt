package com.example.task1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BroadcastNotifyMovieDeleted : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action=="deleteMovie"){
            val id = intent.getIntExtra("id",-5)
            if(context is Home){
                context.deleteMovie(id)
                context.updateBookmarkList()
            }
        }

    }

}