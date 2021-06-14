package com.prateek.dogengine.data

class DogBreedsRepository(
    private val mRemoteDataSource: RemoteDogBreedDataSource
) : DogBreedDataSource {

    override fun searchDogBreeds(query: String) = mRemoteDataSource.searchDogBreeds(query)
}