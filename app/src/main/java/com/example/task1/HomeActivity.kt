package com.example.task1

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Callback



class HomeActivity : AppCompatActivity() {
    private var moviesList = mutableListOf<Movie>()
    private lateinit var bookmarkedMoviesList : MutableList<Movie>
    private lateinit var sharedPreferences : SharedPrefManager
    private lateinit var bottomNavBar : BottomNavigationView
    private lateinit var progressBar: ProgressBar



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_screen)

        initialize()
    }

    private fun initialize(){

        wrapViews()
        getAllData()



        bottomNavBar.setOnItemSelectedListener {
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
        bottomNavBar = findViewById(R.id.bottomNav)!!
        bookmarkedMoviesList= mutableListOf()
        sharedPreferences = SharedPrefManager(this)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility=View.VISIBLE
        bottomNavBar.menu.getItem(1).isEnabled = false
    }



   private fun getAllData(){

        val call = ApiClient.apiService.getMovieById()
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                if (response.isSuccessful) {
                    val movies = response.body()
                    if (movies != null) {
                        moviesList= movies.toMutableList()
                        progressBar.visibility=View.GONE
                        loadFragment(HomeFragment())
                    }
                } else {
                    Toast.makeText(this@HomeActivity,"Retrofit error reading data from API",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(this@HomeActivity,"Retrofit error $t",Toast.LENGTH_LONG).show()
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