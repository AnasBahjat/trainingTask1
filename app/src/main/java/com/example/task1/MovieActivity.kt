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
import com.example.task1.databinding.ActivityMovieBinding
import org.w3c.dom.Text
import java.util.ArrayList

class MovieActivity : AppCompatActivity() {

    private lateinit var sharedPreferences : SharedPrefManager
    private lateinit var binding : ActivityMovieBinding

    var id : Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding=ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        wrapViews()
    }

    private fun wrapViews(){
        sharedPreferences= SharedPrefManager(this)
        wrapDataToViews()
        if(sharedPreferences.getMovieSavedStatus(id) == 0){
            binding.bookmarkDis.setImageResource(R.drawable.bookmark_disabled)
        }

        else {
            binding.bookmarkDis.setImageResource(R.drawable.filled_bookmark)
        }

        binding.bookmarkDis.setOnClickListener {
            if (sharedPreferences.getMovieSavedStatus(id) == 0) {
                binding.bookmarkDis.setImageResource(R.drawable.filled_bookmark)
                sharedPreferences.setMovieSavedStatus(id,1)
            }
            else {
                binding.bookmarkDis.setImageResource(R.drawable.bookmark_disabled)
                sharedPreferences.removeMovieFromSharedPref(id)
                val deleteIntent = Intent("deleteMovie")
                deleteIntent.putExtra("id", id)

                sendBroadcast(deleteIntent)
            }
        }

        binding.backImg.setOnClickListener {
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
                .into(binding.movieImg)

            binding.movieName.text=movie.name
            binding.rateText.text=getString(R.string.rating,movie.rating.average.toString())

            val hours = (movie.runtime / 60).toString()
            val mins = (movie.runtime % 60).toString()
            binding.movieLength.text=getString(R.string.movieLength,hours,mins)
            binding.movieLanguage.text=movie.language
            binding.summary.text=movie.summary

            if(!binding.categLayout.isEmpty()){
                binding.categLayout.removeAllViews()
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
                binding.categLayout.addView(textView)

            }
        }
    }

}