package com.prateek.dogengine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prateek.dogengine.apis.ApiClient
import com.prateek.dogengine.apis.ApiService
import com.prateek.dogengine.data.Breed
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * ViewModel class for our Dog Breed list.
 * It contains a mutable live data list of dog breed which can be observe upon.
 * An ApiService instance is used to fetch data.
 * Once fetched, the observer is called upon to update the UI.
 */
class BreedViewModel : ViewModel() {
    private val mApiService = ApiClient.getClient().create(ApiService::class.java)
    private var mCalls: Call<List<Breed>>? = null
    private var mBreedList: MutableLiveData<List<Breed>>? = MutableLiveData()
    private var mError: MutableLiveData<String>? = MutableLiveData()

    fun getBreeds(): LiveData<List<Breed>>? {
        return mBreedList
    }

    fun getError(): LiveData<String?>? {
        return mError
    }

    fun searchBreeds(query: String) {
        /*
        Below call is made to update the observer with a null value so that current list can be
        cleared before fetching and loading any new data being requested now
         */
        mBreedList?.postValue(null)

        /*
        It wil cancel any existing request. A user may request multiple calls before a response for
        previous request is served.
         */
        mCalls?.cancel()

        //ApiService to fetch list of breeds based on search query
        mCalls = mApiService.searchBreeds(query)

        mCalls?.enqueue(object : Callback<List<Breed>?> {
            override fun onResponse(call: Call<List<Breed>?>, response: Response<List<Breed>?>) {
                //Update the observer about the response results
                mBreedList?.postValue(response.body())
            }

            override fun onFailure(call: Call<List<Breed>?>, t: Throwable) {
                //Update the observer about the error to be handled accordingly
                mError?.postValue(t.localizedMessage)
            }
        })
    }
}