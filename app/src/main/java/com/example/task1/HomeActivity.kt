package com.example.task1

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.task1.databinding.HomeScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class HomeActivity : AppCompatActivity() {
    private var moviesList = mutableListOf<Movie>()
    private lateinit var bookmarkedMoviesList : MutableList<Movie>
    private lateinit var sharedPreferences : SharedPrefManager
    private lateinit var myAlertDialog: MyAlertDialog
    private lateinit var binding :  HomeScreenBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = HomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initialize()
    }

    private fun initialize(){
        wrapViews()
        getAllData()



        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.homeMovies -> {
                    loadFragment(HomeFragment())
                true}
                R.id.bookmarkMovies ->{
                    loadFragment(BookmarkedMoviesFragment())
                    true
                }
                else -> {
                    true
                }
            }
        }


    }


    private fun wrapViews(){
        bookmarkedMoviesList= mutableListOf()
        sharedPreferences = SharedPrefManager(this)
        binding.progressBar.visibility=View.VISIBLE
        binding.bottomNav.menu.getItem(1).isEnabled = false
        myAlertDialog = MyAlertDialog(this)
    }



   private fun getAllData(){

        val call = ApiClient.apiService.getMovieById()
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                if (response.isSuccessful) {
                    val movies = response.body()
                    if (movies != null) {
                        moviesList= movies.toMutableList()
                        binding.progressBar.visibility=View.GONE
                        loadFragment(HomeFragment())
                    }
                } else {
                    Toast.makeText(this@HomeActivity,"Retrofit error reading data from API",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                binding.progressBar.visibility=View.GONE

                when (t){
                    is SocketTimeoutException ->  myAlertDialog.showAlertDialog("Connection Error","Connection Timeout , try again later",R.drawable.error_socket_red)
                    is UnknownHostException -> myAlertDialog.showAlertDialog("Network Error","Please Check your internet connection ..",R.drawable.no_wifi_red)
                    else -> myAlertDialog.showAlertDialog("Error occurred","There are some problem , please try again later",R.drawable.baseline_error_24)
                }

            }
        })
    }


    private fun loadFragment(fragment : Fragment){
        val bundle = Bundle().apply {
            putParcelableArrayList("moviesList", ArrayList(moviesList))
        }
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentsContainer,fragment)
        transaction.commit()
    }



}