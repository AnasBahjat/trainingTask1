package com.example.task1

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.DefaultRetryPolicy
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
    private lateinit var recyclerView2 : RecyclerView
    private lateinit var myCustomAdapter: CustomeAdapter
    private lateinit var myList :List<String>
    private val URL = "https://api.tvmaze.com/shows"
    private lateinit var  myAdapter : CustomeAdapter
    private lateinit var requestQueue : RequestQueue
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



    private val someActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val value = data?.getIntExtra("deletedID",-5)
            val deletedFlag = data?.getBooleanExtra("deletedFlag",false)

            if(value != null && deletedFlag == true){
                val movie = getMovieData(value)
                Log.d("The length is ${bookmarkedMoviesList.size}","The length is ${bookmarkedMoviesList.size}")
                if(bookmarkedMoviesList.size > 1){
                    Log.d("The length is ${bookmarkedMoviesList[0].id}  ${bookmarkedMoviesList[0].name}","The length is ${bookmarkedMoviesList[0].id}  ${bookmarkedMoviesList[0].name}")
                    bookmarkedMoviesList.remove(movie)
                    myCustomAdapter=CustomeAdapter(this,this,bookmarkedMoviesList)
                    recyclerView.adapter=myCustomAdapter
                }
                else {
                    Log.d("Empty !!!!!","Empty !!!!!")
                    emptyBookmarkedActivity = EmptyBookMarkedAdapter()
                    recyclerView.adapter=emptyBookmarkedActivity
                }
               // myCustomAdapter.notifyItemRemoved(index)
                //recyclerView.adapter=myCustomAdapter
            }
        }
    }




    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.home_screen)
        wrapViews()
        recyclerView=findViewById(R.id.recView)

        requestQueue= Volley.newRequestQueue(this)
        recyclerView.layoutManager=LinearLayoutManager(this)

        bookmarkedMoviesList= mutableListOf<Movie>()
        getAllData()
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
                moviesList= movies.toMutableList()
            }
            catch (e:Exception){
                Toast.makeText(this,"error $e",Toast.LENGTH_LONG).show()
            }
        }) { error ->
            Toast.makeText(this, "Volley error $error", Toast.LENGTH_LONG).show()
        }
        requestQueue.add(stringRequest)
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

        someActivityResultLauncher.launch(intent)

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
                Log.d("---------<><><> Key is $key","---------<><><> Key is $key")
                val movie = getMovieData(key.toInt())
                if (movie != null) {
            //        Log.d("Movie id to be added is ${movie.id}","Movie id to be added is ${movie.id}\"")
                    bookmarkedMoviesList.add(movie)
                }
            }
        }
        if(bookmarkedMoviesList.isEmpty()){
            emptyBookmarkedActivity= EmptyBookMarkedAdapter()
            recyclerView.adapter=emptyBookmarkedActivity
        }
        else {
            //moviesList.clear()
            myCustomAdapter=CustomeAdapter(this,this,bookmarkedMoviesList)
            Log.d("${bookmarkedMoviesList.size}","${bookmarkedMoviesList.size}")
            recyclerView.adapter=myCustomAdapter
        }
    }

    fun bookmarkedImageClicked(view: View) {
        mainPageImg.alpha = 0.5f
        bookmarkImg.setImageResource(R.drawable.bookmark_enabled)
        updateBookmarkList()
    }


}