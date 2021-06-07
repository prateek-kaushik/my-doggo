package com.prateek.dogengine.data

import com.prateek.dogengine.apis.ApiClient
import com.prateek.dogengine.apis.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteDogBreedDataSource {
    private val mApiService = ApiClient.getClient().create(ApiService::class.java)

    fun searchDogBreeds(query: String, callback: DogBreedDataSource.BreedsLoadCallback) {

        val call: Call<List<Breed>?> = mApiService.searchBreeds(query);

        call.enqueue(object : Callback<List<Breed>?> {
            override fun onResponse(call: Call<List<Breed>?>, response: Response<List<Breed>?>) {
                response.body()?.let { callback.onBreedsLoaded(it) }
                    ?: callback.onError(Throwable("Null list received"))
            }

            override fun onFailure(call: Call<List<Breed>?>, t: Throwable) {
                callback.onError(t)
            }
        })
    }
}