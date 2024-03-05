package com.example.task1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import org.json.JSONArray
import java.lang.Exception


class Home : AppCompatActivity() , MovieClicked{
    private lateinit var recyclerView : RecyclerView
    private lateinit var myCustomAdapter: CustomeAdapter
    private lateinit var myList :List<String>
    private val URL = "https://api.tvmaze.com/shows"
    private lateinit var  myAdapter : CustomeAdapter
    private lateinit var requestQueue : RequestQueue
    private lateinit var data : List<Movie>
    private lateinit var catLinearLayout: LinearLayout
    private lateinit var mainPageImg : ImageView
    private lateinit var bookmarkImg : ImageView
    lateinit var sharedPrefManager: SharedPreferences
    lateinit var editor : SharedPreferences.Editor
    val moviesList = mutableListOf<Movie>()
    lateinit var movie : Movie
    lateinit var bookmarkedMoviesList : MutableList<Movie>
    lateinit var bookmarkedMovie : Movie
    lateinit var emptyBookmarkedActivity: EmptyBookMarkedAdapter


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_screen)
        wrapViews()
        recyclerView=findViewById(R.id.recView)

        requestQueue= Volley.newRequestQueue(this)
        recyclerView.layoutManager=LinearLayoutManager(this)

        recyclerView.setHasFixedSize(true)

        getAllData()

        bookmarkImg.setOnClickListener{
            bookmarkImg.setImageResource(R.drawable.bookmark_enabled)
            updateBookmarkList()
        }

    }

    fun wrapViews(){
        mainPageImg=findViewById(R.id.mainPage)
        bookmarkImg=findViewById(R.id.bookmarkActivityImg)
        sharedPrefManager=getSharedPreferences("taskSharedPreference", Context.MODE_PRIVATE)
        editor=sharedPrefManager.edit()
    }


    fun getAllData(){
        val stringRequest = StringRequest(Request.Method.GET,URL, Response.Listener<String>{ response->
            try {
                val gson= Gson()
                val movies = gson.fromJson(response, Array<Movie>::class.java).toList()
                myCustomAdapter= CustomeAdapter(this,this,movies)
                recyclerView.adapter=myCustomAdapter
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()){
                    val json = jsonArray.getJSONObject(i)
                    val id = json.getInt("id")
                    val name = json.getString("name")
                    val language=json.getString("language")
                    val genres = json.getJSONArray("genres")
                    val genresList = mutableListOf<String>()
                    for(j in 0 until genres.length()){
                        genresList.add(genres.getString(j))
                    }
                    val runtime = json.getInt("runtime")
                    val rating = json.getJSONObject("rating").getDouble("average")

                    val images = json.getJSONObject("image")
                    val mediumImageUrl = images.getString("medium")
                    val originalImageUrl = images.getString("original")
                    val summary = json.getString("summary")



                    movie = Movie(id,name,language,genresList,runtime,Rating(rating),Image(mediumImageUrl,originalImageUrl),summary)
                    moviesList.add(movie)
                }
            }
            catch (e:Exception){

            }
        }) { error ->
            Toast.makeText(this, "Volley error $error", Toast.LENGTH_LONG).show()
        }
        requestQueue.add(stringRequest)
    }

    fun mainPageClicked(view: View) {
        getAllData()
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
        startActivityForResult(intent,123)
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

    private fun updateBookmarkList(){
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
            moviesList.clear()
            myCustomAdapter=CustomeAdapter(this,this,bookmarkedMoviesList)
            recyclerView.adapter=myCustomAdapter
        }
    }
}