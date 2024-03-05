package com.example.task1

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isEmpty
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.flow.combineTransform

class CustomeAdapter(private val movieClickedListener : MovieClicked,private val context : Context, private val movies : List<Movie>) : RecyclerView.Adapter<CustomeAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomeAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_card_view,parent,false)
        return MyViewHolder(view)

    }

    @SuppressLint("SetTextI18n", "ResourceAsColor", "SuspiciousIndentation")
    override fun onBindViewHolder(holder: CustomeAdapter.MyViewHolder, position: Int) {
        val movie = movies[position]

        if(movie.name.length >= 15){
            holder.movieTitle.textSize=20f
            val params = holder.movieRate.layoutParams as ViewGroup.MarginLayoutParams
            params.topMargin=200
            val params1 = holder.rateImg.layoutParams as ViewGroup.MarginLayoutParams
            params1.topMargin=200
        }
        holder.movieTitle.text = movie.name


        if (!holder.categLayout.isEmpty()) {
            holder.categLayout.removeAllViews()
        }



        for (i in movie.genres.indices) {
            val textView = TextView(context)
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
            )

            textView.layoutParams = layoutParams
            textView.text = movie.genres[i]

            val textSize = when {
                movie.genres[i].length <= 8 -> 15f
                else -> 12f
            }

            val width = 300
            val height = 120


            layoutParams.rightMargin = 7
            textView.width = width
            textView.height = height

            textView.textSize = textSize
            val textColor = ContextCompat.getColor(context, R.color.textColor)
            textView.setTextColor(textColor)
            textView.gravity = Gravity.CENTER
            textView.background = ContextCompat.getDrawable(context, R.drawable.custom_btn)
            holder.categLayout.addView(textView)

            holder.movieCardView.setOnClickListener{
                movieClickedListener.onMovieClicked(movie)
            }
        }

        val hours = movie.runtime / 60
        val mins = movie.runtime % 60

        holder.movieDuration.text = "${hours}h ${mins}m"
        holder.movieRate.text = "${movie.rating.average}/10.0"


        val imageUrl =movie.image.medium
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.loading_icon)
                .error(R.drawable.baseline_error_24)
                .into(holder.movieImg)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val movieImg : ImageView = view.findViewById(R.id.movieImage)
        val rateImg : ImageView = view.findViewById(R.id.rateImg)
        val movieTitle : TextView = view.findViewById(R.id.movieTitle)
        val movieRate : TextView = view.findViewById(R.id.movieRate)
        val movieDuration : TextView = view.findViewById(R.id.duration)
        val categLayout : LinearLayout = view.findViewById(R.id.categoryLayout)
        val movieCardView : CardView = view.findViewById(R.id.movieCardView)
    }

}

interface MovieClicked {
    fun onMovieClicked(movieData : Movie)
}