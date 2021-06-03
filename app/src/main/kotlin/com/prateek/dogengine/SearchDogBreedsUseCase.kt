package com.prateek.dogengine

import com.prateek.dogengine.data.Breed
import com.prateek.dogengine.data.DogBreedDataSource

class SearchDogBreedsUseCase(val mDataSource: DogBreedDataSource) :
    UseCase<SearchDogBreedsUseCase.RequestData, SearchDogBreedsUseCase.ResponseData>() {

    class RequestData(val query: String) : UseCase.RequestData()

    class ResponseData(val breeds: List<Breed>) : UseCase.ResponseData()

    override fun execute(requestData: RequestData) {
        mRequestData = requestData

        mDataSource.searchDogBreeds(mRequestData?.query ?: "",
            object : DogBreedDataSource.BreedsLoadCallback {
                override fun onBreedsLoaded(breeds: List<Breed>) {
                    mUseCaseCallback?.onSuccess(ResponseData(breeds))
                }

                override fun onError(t: Throwable) {
                    mUseCaseCallback?.onError(t)
                }

            })
    }
}