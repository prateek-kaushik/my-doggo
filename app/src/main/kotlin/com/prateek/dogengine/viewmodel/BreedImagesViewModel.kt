package com.prateek.dogengine.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.prateek.dogengine.data.BreedImage
import com.prateek.dogengine.data.BreedImageDataSource
import com.prateek.dogengine.data.BreedImageDataSourceFactory

/**
 * The ViewModel class for dog breed images.
 * This makes use of Android's Jetpack's Paging library to load the images since images API service
 * supports pagination.
 * First it instantiates a DataSource.Factory instance to create and set up our data source.
 * Then we build our config that further helps in building our LivePagedListBuilder.
 * After that our pagination has been set up and ready to use.
 *
 *
 * In our case we have set setInitialLoadSizeHint as double of PAGE_SIZE which means initially 2
 * pages will be fetched.
 * setPrefetchDistance has been set to a value of 2 which means how far from the edge of loaded
 * content an access must be to trigger further loading.
 */
class BreedImagesViewModel(breedId: Int) : ViewModel() {
    var mBreedsDSF: BreedImageDataSourceFactory = BreedImageDataSourceFactory(breedId)
    var breedImages: LiveData<PagedList<BreedImage>>

    init {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(BreedImageDataSource.PAGE_SIZE * 2)
            .setPageSize(BreedImageDataSource.PAGE_SIZE)
            .setPrefetchDistance(2)
            .build()
        breedImages = LivePagedListBuilder(mBreedsDSF, config).build()
    }
}