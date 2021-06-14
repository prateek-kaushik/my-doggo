package com.prateek.dogengine

import com.prateek.dogengine.data.Breed
import com.prateek.dogengine.data.DogBreedDataSource
import io.reactivex.Observable

class SearchDogBreedsUseCase(
    private val mDataSource: DogBreedDataSource
) : UseCase<SearchDogBreedsUseCase.RequestData, SearchDogBreedsUseCase.ResponseData>() {

    override fun execute(requestData: RequestData): ResponseData {
        mRequestData = requestData

        return ResponseData(mDataSource.searchDogBreeds(mRequestData.query))
    }

    class RequestData(val query: String) : UseCase.RequestData()

    class ResponseData(private val response: Observable<List<Breed>>) : UseCase.ResponseData() {
        fun getResponse() = response
    }
}