package com.prateek.dogengine.data

interface DogBreedDataSource {

    interface BreedsLoadCallback {
        fun onBreedsLoaded(breeds: List<Breed>)

        fun onError(t: Throwable)
    }

    fun searchDogBreeds(query: String, callback: BreedsLoadCallback)
}