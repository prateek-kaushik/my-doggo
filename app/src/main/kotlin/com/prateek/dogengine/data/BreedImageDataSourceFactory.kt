package com.prateek.dogengine.data

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource

class BreedImageDataSourceFactory(private val breedId: Int) :
    DataSource.Factory<Int, BreedImage>() {
    private var breedImageDataSource: BreedImageDataSource? = null
    private val liveData = MutableLiveData<BreedImageDataSource>()

    override fun create(): DataSource<Int?, BreedImage?> {
        breedImageDataSource = BreedImageDataSource(breedId)
        liveData.postValue(breedImageDataSource)
        return breedImageDataSource!!
    }
}