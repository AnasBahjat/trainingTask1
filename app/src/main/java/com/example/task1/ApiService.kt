package com.example.task1


import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("shows")
    fun getPostById(): Call<List<Movie>>
}