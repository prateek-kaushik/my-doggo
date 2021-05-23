package com.prateek.dogengine.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * Our custom ViewModelProvider.Factory since in our use case we have a parameterized constructor.
 * So we need to override the default behaviour to make the framework accept arguments when
 * instantiating ViewModels.
 * In our case, the argument or parameter is the breed id for which user wants to load images
 */
public class BreedImageViewModelFactory implements ViewModelProvider.Factory {
    private int mParam;

    public BreedImageViewModelFactory(int param) {
        mParam = param;
    }

    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BreedImagesViewModel(mParam);
    }
}
