package com.prateek.dogengine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prateek.dogengine.SearchDogBreedsUseCase
import com.prateek.dogengine.UseCase
import com.prateek.dogengine.UseCaseHandler
import com.prateek.dogengine.data.Breed

/**
 * ViewModel class for our Dog Breed list.
 * It contains a mutable live data list of dog breed which can be observe upon.
 * An ApiService instance is used to fetch data.
 * Once fetched, the observer is called upon to update the UI.
 */
class BreedViewModel(private val mUseCaseHandler: UseCaseHandler) : ViewModel() {
    private var mBreedList: MutableLiveData<List<Breed>> = MutableLiveData()
    private var mError: MutableLiveData<String> = MutableLiveData()

    fun getBreeds(): LiveData<List<Breed>> {
        return mBreedList
    }

    fun getError(): LiveData<String?> {
        return mError
    }

    fun searchBreeds(
        useCase: SearchDogBreedsUseCase,
        query: String
    ) {
        mBreedList.value = null

        val requestData = SearchDogBreedsUseCase.RequestData(query)

        mUseCaseHandler.execute(useCase, requestData,
            object : UseCase.UseCaseCallback<SearchDogBreedsUseCase.ResponseData> {
                override fun onSuccess(response: SearchDogBreedsUseCase.ResponseData) {
                    mBreedList.postValue(response.breeds)
                }

                override fun onError(t: Throwable) {
                    mError.postValue(t.message)
                }
            })
    }
}