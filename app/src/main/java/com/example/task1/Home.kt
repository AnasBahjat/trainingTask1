package com.example.task1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback


class Home : AppCompatActivity() , MovieClicked{
    private lateinit var recyclerView : RecyclerView
    private lateinit var recyclerView2 : RecyclerView
    private lateinit var myCustomAdapter: CustomeAdapter
    private lateinit var myList :List<String>
    private val URL = "https://api.tvmaze.com/shows"
    private lateinit var  myAdapter : CustomeAdapter
    private lateinit var data : List<Movie>
    private lateinit var catLinearLayout: LinearLayout
    private lateinit var mainPageImg : ImageView
    private lateinit var bookmarkImg : ImageView
    private lateinit var sharedPrefManager: SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private var moviesList = mutableListOf<Movie>()
    private lateinit var movie : Movie
    private lateinit var bookmarkedMoviesList : MutableList<Movie>
    private lateinit var bookmarkedMovie : Movie
    private lateinit var emptyBookmarkedActivity: EmptyBookMarkedAdapter
    lateinit var receiver : BroadcastReceiver



    /*private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val value = data?.getIntExtra("deletedID",-5)
            val deletedFlag = data?.getBooleanExtra("deletedFlag",false)

            if(value != null && deletedFlag == true){
                val movie = getMovieData(value)
                if(bookmarkedMoviesList.size > 1){
                    bookmarkedMoviesList.remove(movie)
                    myCustomAdapter=CustomeAdapter(this,this,bookmarkedMoviesList)
                    recyclerView.adapter=myCustomAdapter
                }
                else {
                    emptyBookmarkedActivity = EmptyBookMarkedAdapter()
                    recyclerView.adapter=emptyBookmarkedActivity
                }

            }
        }
    }*/




    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_screen)
        wrapViews()
        recyclerView=findViewById(R.id.recView)

        recyclerView.layoutManager=LinearLayoutManager(this)

        bookmarkedMoviesList= mutableListOf<Movie>()


        val receiver  = BroadcastNotifyMovieDeleted()
        registerReceiver(receiver , IntentFilter("deleteMovie"), RECEIVER_NOT_EXPORTED)


        getAllData()
    }


    private fun wrapViews(){
        mainPageImg=findViewById(R.id.mainPage)
        bookmarkImg=findViewById(R.id.bookmarkActivityImg)
        sharedPrefManager=getSharedPreferences("taskSharedPreference", Context.MODE_PRIVATE)
        editor=sharedPrefManager.edit()
    }



    private fun getAllData(){

        val call = ApiClient.apiService.getPostById()
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                if (response.isSuccessful) {
                    val movies = response.body()

                    if (movies != null) {
                        myCustomAdapter= CustomeAdapter(this@Home,this@Home,movies)
                        recyclerView.adapter=myCustomAdapter
                        moviesList= movies.toMutableList()
                    }
                } else {
                    Toast.makeText(this@Home,"Retrofit error reading data from API",Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                Toast.makeText(this@Home,"Retrofit error ${t.printStackTrace()}",Toast.LENGTH_LONG).show()
            }

        })
    }


    fun mainPageClicked(view: View) {
        mainPageImg.alpha = 1f
        myCustomAdapter= CustomeAdapter(this,this,moviesList)
        recyclerView.adapter=myCustomAdapter
        bookmarkImg.setImageResource(R.drawable.bookmark_disabled)
    }

    override fun onMovieClicked(movieData : Movie) {
        val intent = Intent(this@Home,MovieActivity::class.java)
        val movieName = movieData.name
        val movieImage = movieData.image.original
        val hours = movieData.runtime / 60
        val mins = movieData.runtime % 60
        val language = movieData.language
        val rating = movieData.rating.average
        val summary = movieData.summary
        val genres : List<String> = movieData.genres

        intent.putExtra("id",movieData.id)
        intent.putExtra("movieName",movieName)
        intent.putExtra("movieImage",movieImage)
        intent.putExtra("hours",hours)
        intent.putExtra("mins",mins)
        intent.putExtra("language",language)
        intent.putExtra("rating",rating)
        intent.putExtra("summary",summary)
        intent.putExtra("genres",ArrayList(genres))

       // someActivityResultLauncher.launch(intent)
        startActivity(intent)

    }


    private fun getMovieData(id : Int) : Movie?{
        var bookmarkedMovie: Movie? = null
        for (movie in moviesList){
            if(movie.id==id){
                val name = movie.name
                val language=movie.language
                val genres = movie.genres
                val runtime = movie.runtime
                val rating = movie.rating
                val images = movie.image
                val summary = movie.summary
                bookmarkedMovie = Movie(id,name,language,genres,runtime,rating,images,summary)
                break
            }
        }
        return bookmarkedMovie
    }

     fun updateBookmarkList(){
        bookmarkedMoviesList = mutableListOf()
        for (key in sharedPrefManager.all.keys){
            if(key.toIntOrNull() != null ){
                val movie = getMovieData(key.toInt())
                if (movie != null) {
                    bookmarkedMoviesList.add(movie)
                }
            }
        }

        if(bookmarkedMoviesList.isEmpty()){
            emptyBookmarkedActivity= EmptyBookMarkedAdapter()
            recyclerView.adapter=emptyBookmarkedActivity
        }

        else {

            myCustomAdapter=CustomeAdapter(this,this,bookmarkedMoviesList)
            recyclerView.adapter=myCustomAdapter
        }
    }

    fun bookmarkedImageClicked(view: View) {
        mainPageImg.alpha = 0.5f
        bookmarkImg.setImageResource(R.drawable.bookmark_enabled)
        updateBookmarkList()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    fun deleteMovie(movieID : Int){
        val movieToDelete=getMovieData(movieID)
        Log.d("The list data \n $bookmarkedMoviesList","The list data \n $bookmarkedMoviesList")
        if(bookmarkedMoviesList.isNotEmpty()){
            Log.d("--------------------------------->${bookmarkedMoviesList.size}","--------------------------------->${bookmarkedMoviesList.size}")
            bookmarkedMoviesList.remove(movieToDelete)
        }
    }
}