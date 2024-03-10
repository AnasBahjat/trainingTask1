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
    private lateinit var sharedPrefManager: SharedPreferences
    private lateinit var editor : Editor
    private var deletedFlag = false
    val bookmarkedList = ArrayList<Movie>()
    var id : Int = -1
    var bookmarkFlag = 0
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
        wrapDataToViews()



        if(sharedPrefManager.getInt("$id",0)==0){
            bookmarkImg.setImageResource(R.drawable.bookmark_disabled)
        }
        else {
            bookmarkImg.setImageResource(R.drawable.filled_bookmark)
        }

        bookmarkImg.setOnClickListener {
            if (sharedPrefManager.getInt("$id",0) == 0) {
                bookmarkImg.setImageResource(R.drawable.filled_bookmark)
                deletedFlag=false
                editor.putInt("$id",1).apply()

            } else {
                bookmarkImg.setImageResource(R.drawable.bookmark_disabled)
               // editor.putInt("$id",0).apply()
               // deletedFlag=true
                editor.remove("$id").apply()

                val deleteIntent = Intent()
                deleteIntent.action = "deleteMovie"
                deleteIntent.putExtra("id", id)
                sendBroadcast(deleteIntent)
            }
        }

        backImage.setOnClickListener{
            /*val resultIntent = Intent()
            resultIntent.putExtra("deletedID",id)
            resultIntent.putExtra("deletedFlag",deletedFlag)
            setResult(Activity.RESULT_OK,resultIntent)
            finishActivity(Activity.RESULT_OK)*/
            finish()
        }
    }

    fun wrapViews(){
        movieImage=findViewById(R.id.movieImg)
        backImage=findViewById(R.id.backImg)
        bookmarkImg=findViewById(R.id.bookmarkDis)
        movieTitle=findViewById(R.id.movieName)
        rateText=findViewById(R.id.rateText)
        categoryLayout=findViewById(R.id.categLayout)
        movieLength=findViewById(R.id.movieLength)
        movieLanguage=findViewById(R.id.movieLanguage)
        summary=findViewById(R.id.summary)
        sharedPrefManager=getSharedPreferences("taskSharedPreference", Context.MODE_PRIVATE)
        editor=sharedPrefManager.edit()
    }


    fun wrapDataToViews(){
        id = intent.getIntExtra("id",-1)

        Log.d("---------> $id","---------> $id")
        Glide.with(this)
            .load(intent.getStringExtra("movieImage"))
            .placeholder(R.drawable.loading_icon)
            .error(R.drawable.baseline_error_24)
            .into(movieImage)

        movieTitle.text=intent.getStringExtra("movieName")

        val rate : Double = intent.getDoubleExtra("rating",0.0)

        rateText.text="$rate/10.0"

        movieLength.text="${intent.getIntExtra("hours",0)}h ${intent.getIntExtra("mins",0)}min"

        movieLanguage.text=intent.getStringExtra("language")

        summary.text=intent.getStringExtra("summary")

        val genres : ArrayList<String>? = intent.getStringArrayListExtra("genres")
        Log.d("$genres","$genres")

        if(!categoryLayout.isEmpty()){
            categoryLayout.removeAllViews()
        }

        if (genres != null) {
            for (x in genres.indices){
                val textView = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                )
                textView.text = genres[x]

                var textSize = when {
                    genres[x].length <= 8 -> 16f
                    else -> 12f
                }

                if(genres[x].length > 13){
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

    override fun onBackPressed() {
        /*val resultIntent = Intent()
        resultIntent.putExtra("deletedID",id)
        resultIntent.putExtra("deletedFlag",deletedFlag)
        setResult(Activity.RESULT_OK,resultIntent)
        finishActivity(Activity.RESULT_OK)*/
        finish()
        super.onBackPressed()
    }
}