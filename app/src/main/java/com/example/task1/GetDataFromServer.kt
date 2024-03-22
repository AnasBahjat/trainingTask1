package com.example.task1

import android.content.Context
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GetDataFromServer(val context : Context, private val listener : DataIsReady,stopTheApp: StopTheApp) {

    private var moviesList = mutableListOf<Movie>()
    private var myAlertDialog = MyAlertDialog(context,stopTheApp)
    private var dataReady : DataIsReady? = null

    fun getAllData(){
        dataReady=listener
        val call = ApiClient.apiService.getMovieById()
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                if (response.isSuccessful) {
                    val movies = response.body()
                    if (movies != null) {
                        moviesList= movies.toMutableList()
                        dataReady?.dataReadyToBeDisplayed(moviesList)
                    }
                } else {
                    Toast.makeText(context,"Retrofit error reading data from API", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                when (t){
                    is SocketTimeoutException ->  myAlertDialog.showAlertDialog("Connection Error","Connection Timeout , try again later",R.drawable.error_socket_red)
                    is UnknownHostException -> myAlertDialog.showAlertDialog("Network Error","Please Check your internet connection ..",R.drawable.no_wifi_red)
                    else -> myAlertDialog.showAlertDialog("Error occurred","There are some problem , please try again later",R.drawable.baseline_error_24)
                }
            }
        })
    }
}