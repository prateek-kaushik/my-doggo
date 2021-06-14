package com.prateek.dogengine.data

import com.prateek.dogengine.apis.ApiClient
import com.prateek.dogengine.apis.ApiService

class RemoteDogBreedDataSource {
    private val mApiService = ApiClient.getClient().create(ApiService::class.java)

    fun searchDogBreeds(query: String) = mApiService.searchBreeds(query)
}