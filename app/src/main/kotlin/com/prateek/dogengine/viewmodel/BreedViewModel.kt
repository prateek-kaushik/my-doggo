package com.prateek.dogengine.viewmodel

import androidx.lifecycle.ViewModel
import com.prateek.dogengine.SearchDogBreedsUseCase
import com.prateek.dogengine.UseCaseHandler
import com.prateek.dogengine.data.Breed
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

/**
 * ViewModel class for our Dog Breed list.
 * It contains a mutable live data list of dog breed which can be observe upon.
 * An ApiService instance is used to fetch data.
 * Once fetched, the observer is called upon to update the UI.
 */
class BreedViewModel(private val mUseCaseHandler: UseCaseHandler) : ViewModel() {
    private var mBreedList: PublishSubject<List<Breed>> = PublishSubject.create()
    private var mDisposable: Disposable? = null

    fun getBreeds(): Observable<List<Breed>> = mBreedList

    fun searchBreeds(
        useCase: SearchDogBreedsUseCase,
        query: String
    ) {
        val requestData = SearchDogBreedsUseCase.RequestData(query)

        val response = mUseCaseHandler.execute(useCase, requestData).getResponse()

        response.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : Observer<List<Breed>> {
                override fun onSubscribe(d: Disposable) {
                    mDisposable = d
                }

                override fun onNext(list: List<Breed>) {
                    mBreedList.onNext(list)
                }

                override fun onError(e: Throwable) {
                    mBreedList.onError(e)
                }

                override fun onComplete() {
                    //nothing to do
                }
            })
    }

    override fun onCleared() {
        super.onCleared()
        mDisposable?.dispose()
    }
}