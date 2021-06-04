package com.prateek.dogengine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.prateek.dogengine.UseCaseHandler

class BreedViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BreedViewModel(UseCaseHandler.getInstance()) as T
    }
}