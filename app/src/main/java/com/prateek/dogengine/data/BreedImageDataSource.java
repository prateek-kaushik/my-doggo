package com.prateek.dogengine.data;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.prateek.dogengine.apis.ApiClient;
import com.prateek.dogengine.apis.ApiService;

import java.util.List;

import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
public class BreedImageDataSource extends PageKeyedDataSource<Integer, BreedImage> {
    /*
    This page size value denotes how many results can the API response include at most.
    In our current use case, I have set it to a value as small as 2 since the API that have been
    used here do not return large result sets for any dog breed selected. Thus a smaller value helps
    in achieving and understanding the pagination concept more precisely.
     */
    public static final int PAGE_SIZE = 2;
    //Provides ApiService instance to make requests
    private final ApiService mApiService = ApiClient.INSTANCE.getClient().create(ApiService.class);
    //the unique od of a breed for which images are requested
    private int mBreedId;
    /*
    mCount denotes the number of images fetched so far. It helps in deciding whether more
    pagination requests are to be made or not if it has reached the limit.
     */
    private int mCount = 0;

    public BreedImageDataSource(int breedId) {
        mBreedId = breedId;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, BreedImage> callback) {
        Call<List<BreedImage>> call = mApiService.searchImages(String.valueOf(mBreedId), 0, PAGE_SIZE);

        call.enqueue(new Callback<List<BreedImage>>() {
            @Override
            public void onResponse(Call<List<BreedImage>> call, Response<List<BreedImage>> response) {
                if (response != null) {
                    List<BreedImage> list = response.body();

                    callback.onResult(list, null, getNextPageKey(response.headers(), list));
                }
            }

            @Override
            public void onFailure(Call<List<BreedImage>> call, Throwable t) {
                //Not handling for now. In our use case it can be used to display error result on UI
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, BreedImage> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, BreedImage> callback) {
        Call<List<BreedImage>> call = mApiService.searchImages(String.valueOf(mBreedId), params.key, params.requestedLoadSize);

        call.enqueue(new Callback<List<BreedImage>>() {
            @Override
            public void onResponse(Call<List<BreedImage>> call, Response<List<BreedImage>> response) {
                if (response != null) {
                    List<BreedImage> list = response.body();

                    callback.onResult(list, getNextPageKey(response.headers(), list));
                }
            }

            @Override
            public void onFailure(Call<List<BreedImage>> call, Throwable t) {
                //Not handling for now. In our use case it can be used to display error result on UI
            }
        });
    }

    private int getPaginationPage(Headers headers) {
        int paginationPage = 0;
        String page = headers.get("Pagination-Page");
        if (page != null) {
            try {
                paginationPage = Integer.parseInt(page);
            } catch (NumberFormatException e) {
            }
        }
        return paginationPage;
    }

    /**
     * Returns us the maximum amount of items that can be fetched
     *
     * @param headers response header
     * @return
     */
    private int maxItemCount(Headers headers) {
        int paginationCount = 0;
        String count = headers.get("Pagination-Count");
        if (count != null) {
            try {
                paginationCount = Integer.parseInt(count);
            } catch (NumberFormatException e) {
            }
        }
        return paginationCount;
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
    private Integer getNextPageKey(Headers headers, List<BreedImage> list) {
        Integer thisPageNumber = null;

        if (list != null && list.size() > 0) {
            mCount += list.size();

            int maxItemCount = maxItemCount(headers);
            thisPageNumber = getPaginationPage(headers);

            if (mCount >= maxItemCount) {
                thisPageNumber = null;
            }
        }

        return thisPageNumber == null ? null : thisPageNumber + 1;
    }
}
