package com.prateek.dogengine.data

class DogBreedsRepository(
    val mRemoteDataSource: RemoteDogBreedDataSource
) : DogBreedDataSource {

    override fun searchDogBreeds(query: String, callback: DogBreedDataSource.BreedsLoadCallback) {
        mRemoteDataSource.searchDogBreeds(query, callback)
    }
}