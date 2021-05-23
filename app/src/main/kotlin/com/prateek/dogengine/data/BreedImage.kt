package com.prateek.dogengine.data

import com.google.gson.annotations.SerializedName

/**
 * Data model class to hold the image for a particular Breed.
 */
public class BreedImage {
    @SerializedName("breeds")
    val mBreed: List<Breed>? = null

    @SerializedName("id")
    val mId: String? = null

    @SerializedName("url")
    val mUrl: String? = null
}