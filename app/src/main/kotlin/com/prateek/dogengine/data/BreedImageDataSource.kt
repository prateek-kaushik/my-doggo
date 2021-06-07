package com.prateek.dogengine.data

import androidx.paging.PageKeyedDataSource
import com.prateek.dogengine.apis.ApiClient.getClient
import com.prateek.dogengine.apis.ApiService
import okhttp3.Headers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Data source class to handle pagination request for dog breed image results.
 * The response header is expected to contain a Pagination-Count field that tells how much items can
 * be loaded at most. Another field Pagination-Page tells about the page number value for current
 * page.
 * With every API call response, both of the above mentioned fields are retrieved to decide whether
 * to request for next page. To simply this, a count value is maintained that tells us about total
 * items loaded so far. We then use this value to check whether is has reached Pagination-Count value.
 * If so, we stop making any further requests.
 */
class BreedImageDataSource(  //the unique od of a breed for which images are requested
    private val mBreedId: Int
) :
    PageKeyedDataSource<Int?, BreedImage?>() {
    //Provides ApiService instance to make requests
    private val mApiService = getClient().create(ApiService::class.java)

    /*
   mCount denotes the number of images fetched so far. It helps in deciding whether more
   pagination requests are to be made or not if it has reached the limit.
    */
    private var mCount = 0
    override fun loadInitial(
        params: LoadInitialParams<Int?>,
        callback: LoadInitialCallback<Int?, BreedImage?>
    ) {
        val call = mApiService.searchImages(mBreedId.toString(), 0, PAGE_SIZE)

        call.enqueue(object : Callback<List<BreedImage>?> {
            override fun onResponse(
                call: Call<List<BreedImage>?>,
                response: Response<List<BreedImage>?>
            ) {
                response.body()?.let {
                    callback.onResult(
                        it,
                        null,
                        getNextPageKey(response.headers(), it)
                    )
                }
            }

            override fun onFailure(call: Call<List<BreedImage>?>, t: Throwable) {
                // Not yet implemented
            }

        })
    }

    override fun loadBefore(params: LoadParams<Int?>, callback: LoadCallback<Int?, BreedImage?>) {}

    override fun loadAfter(params: LoadParams<Int?>, callback: LoadCallback<Int?, BreedImage?>) {
        val call =
            mApiService.searchImages(mBreedId.toString(), params.key, params.requestedLoadSize)

        call!!.enqueue(object : Callback<List<BreedImage>?> {
            override fun onResponse(
                call: Call<List<BreedImage>?>,
                response: Response<List<BreedImage>?>
            ) {
                if (response != null) {
                    val list = response.body()
                    callback.onResult(list!!, getNextPageKey(response.headers(), list))
                }
            }

            override fun onFailure(call: Call<List<BreedImage>?>, t: Throwable) {
                //Not handling for now. In our use case it can be used to display error result on UI
            }
        })
    }

    private fun getPaginationPage(headers: Headers): Int {
        var paginationPage = 0
        val page = headers["Pagination-Page"]
        if (page != null) {
            try {
                paginationPage = page.toInt()
            } catch (e: NumberFormatException) {
            }
        }
        return paginationPage
    }

    /**
     * Returns us the maximum amount of items that can be fetched
     *
     * @param headers response header
     * @return
     */
    private fun maxItemCount(headers: Headers): Int {
        var paginationCount = 0
        val count = headers["Pagination-Count"]
        if (count != null) {
            try {
                paginationCount = count.toInt()
            } catch (e: NumberFormatException) {
            }
        }
        return paginationCount
    }

    /**
     * Returns next page key to be fetched. In our case it is the page number or otherwise a null
     * value if no page is to be fetched. Passing null value tells paging framework to stop making
     * any more requests.
     *
     * @param headers response header
     * @param list    response data list just fetched. Helps to update mCount value
     * @return
     */
    private fun getNextPageKey(headers: Headers, list: List<BreedImage?>?): Int? {
        var thisPageNumber: Int? = null
        if (list != null && list.size > 0) {
            mCount += list.size
            val maxItemCount = maxItemCount(headers)
            thisPageNumber = getPaginationPage(headers)
            if (mCount >= maxItemCount) {
                thisPageNumber = null
            }
        }
        return if (thisPageNumber == null) null else thisPageNumber + 1
    }

    companion object {
        /*
    This page size value denotes how many results can the API response include at most.
    In our current use case, I have set it to a value as small as 2 since the API that have been
    used here do not return large result sets for any dog breed selected. Thus a smaller value helps
    in achieving and understanding the pagination concept more precisely.
     */
        const val PAGE_SIZE = 2
    }
}