package com.prateek.dogengine.apis

import com.prateek.dogengine.data.Breed
import com.prateek.dogengine.data.BreedImage
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("/v1/breeds/search")
    fun searchBreeds(@Query("q") query: String): Observable<List<Breed>>

    @GET("/v1/images/search")
    fun searchImages(
        @Query("breed_id") breedId: String, @Query("page") page: Int,
        @Query("limit") pageSize: Int
    ): Call<List<BreedImage>?>
}