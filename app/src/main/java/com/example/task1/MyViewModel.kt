package com.example.task1

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback

class MyViewModel : ViewModel() {

    private var moviesList = MutableLiveData<MutableList<Movie>>()
    fun readDataFromAPI(){
        val call = ApiClient.apiService.getMovieById()
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                if (response.isSuccessful) {
                    val movies = response.body()
                    if (movies != null) {
                        moviesList.value = movies.toMutableList()
                    }
                } else {
                    Log.e("Something went wrong","Something went wrong")
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Log.e("Error Occurred : $t","Error Occurred : $t")
            }
        })
    }

    fun returnApiData() : LiveData<MutableList<Movie>> {
        return moviesList
    }
}