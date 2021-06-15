package com.prateek.dogengine.data

import com.prateek.dogengine.apis.ApiClient
import com.prateek.dogengine.apis.ApiService
import retrofit2.Call

class RemoteDogBreedDataSource {
    private val mApiService = ApiClient.getClient().create(ApiService::class.java)
    private var mCall: Call<List<Breed>?>? = null

    fun searchDogBreeds(query: String, callback: DogBreedDataSource.BreedsLoadCallback) {
        mCall?.cancel()

        mCall = mApiService.searchBreeds(query)

        try {
            mCall?.execute()?.body()?.apply {
                callback.onBreedsLoaded(this)
            }
        } catch (e: Throwable) {
            callback.onError(e)
        }
    }
}