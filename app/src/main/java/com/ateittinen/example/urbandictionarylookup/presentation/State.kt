package com.ateittinen.example.urbandictionarylookup.presentation

import com.ateittinen.example.urbandictionarylookup.domain.AllUrbanDictionaryDefinitions
import com.ww.roxie.BaseState

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
sealed class State : BaseState {
    object Idle : State()
    data class Loading(val term: String): State()
    data class Loaded(val term: String, val allUrbanDictionaryDefinitions: AllUrbanDictionaryDefinitions) : State()
    data class LoadingError(val term: String, val throwable: Throwable): State()

    data class Sorting(val thumbsUp: Boolean) : State()
    data class Sorted(val thumbsUp: Boolean, val allUrbanDictionaryDefinitions: AllUrbanDictionaryDefinitions) : State()
}