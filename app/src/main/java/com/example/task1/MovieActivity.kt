package com.example.task1

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.CycleInterpolator
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isEmpty
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import org.w3c.dom.Text
import java.util.ArrayList

class MovieActivity : AppCompatActivity() {

    private lateinit var movieImage : ImageView
    private lateinit var backImage : ImageView
    private lateinit var bookmarkImg : ImageView
    private lateinit var movieTitle : TextView
    private lateinit var rateText : TextView
    private lateinit var movieLength : TextView
    private lateinit var categoryLayout : LinearLayout
    private lateinit var movieLanguage : TextView
    private lateinit var summary : TextView
    private lateinit var sharedPreferences : SharedPrefManager

    var id : Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_movie)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        wrapViews()
    }

    private fun wrapViews(){
        movieImage=findViewById(R.id.movieImg)
        backImage=findViewById(R.id.backImg)
        bookmarkImg=findViewById(R.id.bookmarkDis)
        movieTitle=findViewById(R.id.movieName)
        rateText=findViewById(R.id.rateText)
        categoryLayout=findViewById(R.id.categLayout)
        movieLength=findViewById(R.id.movieLength)
        movieLanguage=findViewById(R.id.movieLanguage)
        summary=findViewById(R.id.summary)

        sharedPreferences= SharedPrefManager(this)
        wrapDataToViews()







        if(sharedPreferences.getMovieSavedStatus(id) == 0){
            bookmarkImg.setImageResource(R.drawable.bookmark_disabled)
        }

        else {
            bookmarkImg.setImageResource(R.drawable.filled_bookmark)
        }

        bookmarkImg.setOnClickListener {
            if (sharedPreferences.getMovieSavedStatus(id) == 0) {
                bookmarkImg.setImageResource(R.drawable.filled_bookmark)
                sharedPreferences.setMovieSavedStatus(id,1)
            }
            else {
                bookmarkImg.setImageResource(R.drawable.bookmark_disabled)
                sharedPreferences.removeMovieFromSharedPref(id)
                val deleteIntent = Intent("deleteMovie")
                deleteIntent.putExtra("id", id)

                sendBroadcast(deleteIntent)
            }
        }

        backImage.setOnClickListener {
            finish()
        }
    }


    private fun wrapDataToViews(){
        val movie = intent.getParcelableExtra<Movie>("movie")
        if (movie != null) {
            id=movie.id
            Glide.with(this)
                .load(movie.image.medium)
                .placeholder(R.drawable.loading_icon)
                .error(R.drawable.baseline_error_24)
                .into(movieImage)

            movieTitle.text=movie.name
            rateText.text=getString(R.string.rating,movie.rating.average.toString())

            val hours = (movie.runtime / 60).toString()
            val mins = (movie.runtime % 60).toString()
            movieLength.text=getString(R.string.movieLength,hours,mins)
            movieLanguage.text=movie.language
            summary.text=movie.summary

            if(!categoryLayout.isEmpty()){
                categoryLayout.removeAllViews()
            }
            for (x in movie.genres.indices){
                val textView = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textView.text = movie.genres[x]

                var textSize = when {
                    movie.genres[x].length <= 8 -> 16f
                    else -> 12f
                }

                if(movie.genres[x].length > 13){
                    textView.width = 350
                    textSize=13f
                }
                else {
                    textView.width = 300
                }

                layoutParams.rightMargin = 18

                textView.height = 120
                textView.textSize = textSize
                val textColor = ContextCompat.getColor(this, R.color.textColor)
                textView.setTextColor(textColor)
                textView.gravity = Gravity.CENTER
                textView.background = ContextCompat.getDrawable(this, R.drawable.custom_btn)
                textView.layoutParams = layoutParams
                categoryLayout.addView(textView)

            }
        }
    }

}