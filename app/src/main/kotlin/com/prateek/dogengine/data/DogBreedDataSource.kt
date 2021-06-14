package com.prateek.dogengine.data

import io.reactivex.Observable

interface DogBreedDataSource {

    fun searchDogBreeds(query: String): Observable<List<Breed>>
}