package com.example.task1

import java.util.ArrayList


data class Movie(val id : Int, val name :String , val language : String
                 , val genres : List<String>
                 ,val runtime:Int, val rating : Rating
                 ,val image : Image , val summary : String){
}

data class Image(
        val medium: String,
        val original: String
)

data class Rating(
        val average : Double
)
