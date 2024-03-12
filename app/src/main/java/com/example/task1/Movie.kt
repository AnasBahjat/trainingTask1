package com.example.task1

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList


data class Movie(val id : Int, val name :String , val language : String
                 , val genres : List<String>
                 ,val runtime:Int, val rating : Rating
                 ,val image : Image , val summary : String)
    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.createStringArrayList() ?: ArrayList(),
        parcel.readInt(),
        parcel.readParcelable<Rating>(Rating::class.java.classLoader)!!,
        parcel.readParcelable<Image>(Image::class.java.classLoader)!!,
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(language)
        parcel.writeStringList(genres)
        parcel.writeInt(runtime)
        parcel.writeParcelable(rating, flags)
        parcel.writeParcelable(image, flags)
        parcel.writeString(summary)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}

data class Image(
    val medium: String,
    val original: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(medium)
        parcel.writeString(original)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Image> {
        override fun createFromParcel(parcel: Parcel): Image {
            return Image(parcel)
        }

        override fun newArray(size: Int): Array<Image?> {
            return arrayOfNulls(size)
        }
    }
}


data class Rating(
    val average: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(average)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rating> {
        override fun createFromParcel(parcel: Parcel): Rating {
            return Rating(parcel)
        }

        override fun newArray(size: Int): Array<Rating?> {
            return arrayOfNulls(size)
        }
    }
}
