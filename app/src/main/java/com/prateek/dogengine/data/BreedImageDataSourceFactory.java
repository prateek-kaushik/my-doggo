package com.prateek.dogengine.data;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

public class BreedImageDataSourceFactory extends DataSource.Factory<Integer, BreedImage> {
    private BreedImageDataSource breedImageDataSource;
    private MutableLiveData<BreedImageDataSource> liveData;
    private int mBreedId;

    public BreedImageDataSourceFactory(int breedId) {
        liveData = new MutableLiveData<>();
        mBreedId = breedId;
    }

    @Override
    public DataSource create() {
        breedImageDataSource = new BreedImageDataSource(mBreedId);
        liveData.postValue(breedImageDataSource);
        return breedImageDataSource;
    }
}
