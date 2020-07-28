package com.ateittinen.example.urbandictionarylookup.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ateittinen.example.urbandictionarylookup.domain.LookupUseCase

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
class LookupViewModelFactory(
    private val initialState: State?,
    private val lookupUseCase: LookupUseCase
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LookupViewModel(initialState, lookupUseCase) as T
    }
}