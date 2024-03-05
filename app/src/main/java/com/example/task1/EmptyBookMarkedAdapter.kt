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
import org.w3c.dom.Text

class EmptyBookMarkedAdapter() : RecyclerView.Adapter<EmptyBookMarkedAdapter.MyViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.empty_bookmark,parent,false)
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       // holder.textEmpty.text="Empty Book"
    }


    override fun getItemCount(): Int {
        return 1
    }

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val textEmpty : TextView = view.findViewById(R.id.emptyText)

    }

}