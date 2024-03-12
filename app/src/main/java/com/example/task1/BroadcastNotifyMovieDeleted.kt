package com.example.task1

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BroadcastNotifyMovieDeleted : BroadcastReceiver(){
    override fun onReceive(context: Context, intent: Intent) {
        if(intent.action=="deleteMovie"){
            val id = intent.getIntExtra("id",-5)
            if(context is HomeActivity){
                Log.d("The ID to be deleted is $id","----------> The ID to be deleted is $id")
                context.deleteMovie(id)
                context.updateBookmarkList()
            }
        }

    }

}