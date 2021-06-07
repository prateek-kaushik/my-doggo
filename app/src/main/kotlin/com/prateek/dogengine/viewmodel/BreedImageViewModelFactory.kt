package com.prateek.dogengine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Our custom ViewModelProvider.Factory since in our use case we have a parameterized constructor.
 * So we need to override the default behaviour to make the framework accept arguments when
 * instantiating ViewModels.
 * In our case, the argument or parameter is the breed id for which user wants to load images
 */
class BreedImageViewModelFactory(private val mParam: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BreedImagesViewModel(mParam) as T
    }
}