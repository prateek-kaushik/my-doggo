package com.prateek.dogengine.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prateek.dogengine.SearchDogBreedsUseCase
import com.prateek.dogengine.UseCase
import com.prateek.dogengine.UseCaseHandler
import com.prateek.dogengine.data.Breed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel class for our Dog Breed list.
 * It contains a mutable live data list of dog breed which can be observe upon.
 * An ApiService instance is used to fetch data.
 * Once fetched, the observer is called upon to update the UI.
 */
class BreedViewModel(private val mUseCaseHandler: UseCaseHandler) : ViewModel() {
    private var mBreedList: MutableLiveData<List<Breed>> = MutableLiveData()
    private var mError: MutableLiveData<String> = MutableLiveData()

    fun getBreeds() = mBreedList

    fun getError() = mError

    fun searchBreeds(
        useCase: SearchDogBreedsUseCase,
        query: String
    ) {
        mBreedList.value = null

        val requestData = SearchDogBreedsUseCase.RequestData(query)

        viewModelScope.launch(Dispatchers.IO) {
            mUseCaseHandler.execute(useCase, requestData,
                object : UseCase.UseCaseCallback<SearchDogBreedsUseCase.ResponseData> {
                    override fun onSuccess(response: SearchDogBreedsUseCase.ResponseData) {
                        launch(Dispatchers.Main) {
                            mBreedList.value = response.breeds
                        }
                    }

                    override fun onError(t: Throwable) {
                        launch(Dispatchers.Main) {
                            mError.value = t.message
                        }
                    }
                })
        }
    }
}