package com.example.task1

import android.content.Context
import android.content.SharedPreferences

class SharedPrefManager (private val context : Context){
    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("taskSharedPreferences",Context.MODE_PRIVATE)

    fun getMovieSavedStatus(id : Int) : Int {
            return sharedPreferences.getInt("$id",0)
    }

    fun setMovieSavedStatus(id : Int,value : Int){
            val editor=sharedPreferences.edit()
            editor.putInt("$id",value).apply()
    }

    fun removeMovieFromSharedPref(id : Int){
        val editor=sharedPreferences.edit()
        editor.remove("$id").apply()
    }

    fun getAllKeys() : MutableList<Int>{
        val allKeysList : MutableList<Int> = mutableListOf()
        for ( key in sharedPreferences.all.keys){
            if(key.toIntOrNull() != null){
                allKeysList.add(key.toInt())
            }
        }
        return allKeysList
    }
}