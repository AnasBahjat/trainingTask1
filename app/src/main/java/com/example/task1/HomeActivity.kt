package com.example.task1

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback


class HomeActivity : AppCompatActivity() , MovieClicked{
    private lateinit var recyclerView : RecyclerView
    private lateinit var myCustomAdapter: CustomeAdapter
    private lateinit var mainPageImg : ImageView
    private lateinit var bookmarkImg : ImageView
    private var moviesList = mutableListOf<Movie>()
    private lateinit var bookmarkedMoviesList : MutableList<Movie>
    lateinit var receiver : BroadcastReceiver
    private lateinit var sharedPreferences : SharedPrefManager
    private lateinit var emptyTextView : TextView



    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContentView(R.layout.home_screen)
        initialize()

        // bottom navigation bar
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun initialize(){
        wrapViews()
        getAllData()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun wrapViews(){
        mainPageImg=findViewById(R.id.mainPage)
        bookmarkImg=findViewById(R.id.bookmarkActivityImg)
        recyclerView=findViewById(R.id.recView)
        emptyTextView=findViewById(R.id.emptyText)
        recyclerView.layoutManager=LinearLayoutManager(this)
        bookmarkedMoviesList= mutableListOf<Movie>()
        sharedPreferences = SharedPrefManager(this)


        val receiver  = BroadcastNotifyMovieDeleted()
        registerReceiver(receiver , IntentFilter("deleteMovie"), RECEIVER_NOT_EXPORTED)
    }



    private fun getAllData(){

        val call = ApiClient.apiService.getMovieById()
        call.enqueue(object : Callback<List<Movie>> {
            override fun onResponse(call: Call<List<Movie>>, response: retrofit2.Response<List<Movie>>) {
                if (response.isSuccessful) {
                    val movies = response.body()

                    if (movies != null) {
                        myCustomAdapter= CustomeAdapter(this@HomeActivity,this@HomeActivity,movies)
                        recyclerView.adapter=myCustomAdapter
                        moviesList= movies.toMutableList()
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


    fun mainPageClicked(view: View) {
        mainPageImg.alpha = 1f
        recyclerView.visibility=View.VISIBLE
        emptyTextView.visibility=View.GONE
        myCustomAdapter= CustomeAdapter(this,this,moviesList)
        recyclerView.adapter=myCustomAdapter
        bookmarkImg.setImageResource(R.drawable.bookmark_disabled)
    }

    override fun onMovieClicked(movieData : Movie) { // make one object then pass ..
        val intent = Intent(this@HomeActivity,MovieActivity::class.java)
        intent.putExtra("movie",movieData)
        startActivity(intent)

    }


    private fun getMovieData(id : Int) : Movie?{
        var bookmarkedMovie: Movie? = null
        for (movie in moviesList){
            if(movie.id==id){
                bookmarkedMovie=movie
                break
            }
        }
        return bookmarkedMovie
    }

     fun updateBookmarkList(){
        bookmarkedMoviesList = mutableListOf()
         val myKeys=sharedPreferences.getAllKeys()
        for (key in myKeys){
                val movie = getMovieData(key)
                if (movie != null) {
                    bookmarkedMoviesList.add(movie)
                }
        }

        if(bookmarkedMoviesList.isEmpty()){
           // emptyBookmarkedActivity= EmptyBookMarkedAdapter()
            recyclerView.visibility=View.GONE
            emptyTextView.visibility=View.VISIBLE
           // recyclerView.adapter=emptyBookmarkedActivity
        }

        else {
            recyclerView.visibility=View.VISIBLE
            emptyTextView.visibility=View.GONE
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
        if(bookmarkedMoviesList.isNotEmpty()){
            bookmarkedMoviesList.remove(movieToDelete)
        }
    }
}