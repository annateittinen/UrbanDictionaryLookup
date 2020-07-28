package com.ateittinen.example.urbandictionarylookup.presentation

import com.ateittinen.example.urbandictionarylookup.domain.AllUrbanDictionaryDefinitions

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
sealed class Change {
    data class Loading(val term: String): Change()
    data class Loaded(val term: String, val allUrbanDictionaryDefinitions: AllUrbanDictionaryDefinitions) : Change()
    data class LoadError(val term: String, val throwable: Throwable) : Change()

    data class Sorting(val thumbsUp: Boolean): Change()
    data class Sorted(val thumbsUp: Boolean, val allUrbanDictionaryDefinitions: AllUrbanDictionaryDefinitions) : Change()
}