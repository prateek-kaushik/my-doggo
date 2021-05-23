package com.prateek.dogengine.data

import com.google.gson.annotations.SerializedName

/**
 * This is the data model class to hold data for a Breed of dog.
 */
public class Breed {
    @SerializedName("bred_for")
    val mBredFor: String? = null

    @SerializedName("breed_group")
    val mBreedGroup: String? = null

    @SerializedName("id")
    val mId: Int? = null

    @SerializedName("life_span")
    val mLifeSpan: String? = null

    @SerializedName("name")
    val mName: String? = null

    @SerializedName("origin")
    val mOrigin: String? = null

    @SerializedName("reference_image_id")
    val mRefImageId: String? = null

    @SerializedName("temperament")
    val mTemperament: String? = null

    @SerializedName("height")
    public val mHeight: Height? = null

    @SerializedName("weight")
    val mWeight: Weight? = null

    @SerializedName("image")
    val mImage: Image? = null

    public fun getImageUrl() = mImage?.mUrl ?: "https://cdn2.thedogapi.com/images/$mRefImageId.jpg"

    class Height {
        @SerializedName("imperial")
        val mImperial: String? = null

        @SerializedName("metric")
        val mMetric: String? = null
    }

    class Weight {
        @SerializedName("imperial")
        val mImperial: String? = null

        @SerializedName("metric")
        val mMetric: String? = null
    }

    class Image {
        @SerializedName("id")
        val mId: String? = null

        @SerializedName("url")
        val mUrl: String? = null
    }
}