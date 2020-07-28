package com.ateittinen.example.urbandictionarylookup.domain

import com.ateittinen.example.urbandictionarylookup.data.LookupRepository
import com.ateittinen.example.urbandictionarylookup.data.LookupResponse
import io.reactivex.Single
import java.util.*

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
class LookupUseCase(val lookupRepository: LookupRepository) {

    fun getDefinitions(sortByThumbsUp: Boolean, term: String) : Single<LookupResponse> {
        var lookupResponse: LookupResponse = lookupRepository.getDefinitions(term)
        sortAllDefinitions(sortByThumbsUp, lookupResponse.allUrbanDictionaryDefinitions)
        return Single.just(lookupResponse)
    }

    fun sort(thumbsUp: Boolean,
             allUrbanDictionaryDefinitions: AllUrbanDictionaryDefinitions? = AllUrbanDictionaryDefinitions()) : Single<AllUrbanDictionaryDefinitions> {
        sortAllDefinitions(thumbsUp, allUrbanDictionaryDefinitions)
        return Single.just(allUrbanDictionaryDefinitions)
    }

    fun sortAllDefinitions(thumbsUp: Boolean,
                           allUrbanDictionaryDefinitions: AllUrbanDictionaryDefinitions? = AllUrbanDictionaryDefinitions()) { // : AllUrbanDictionaryDefinitions? {
        if (allUrbanDictionaryDefinitions != null) {
            if (thumbsUp)
                Collections.sort(allUrbanDictionaryDefinitions?.list, MyComparatorThumbsUp())
            else
                Collections.sort(allUrbanDictionaryDefinitions?.list, MyComparatorThumbsDown())
        }
    }
}

class MyComparatorThumbsUp : Comparator<UrbanDictionaryDefinition> {
    override fun compare(p0: UrbanDictionaryDefinition?, p1: UrbanDictionaryDefinition?): Int {
        if ((p0 != null) && (p1 != null)) {
            if (p0.thumbs_up > p1.thumbs_up) return -1
            else if (p0.thumbs_up < p1.thumbs_up) return 1
            return 0
        }
        if ((p0 != null) && (p1 == null)) return -1
        if ((p0 == null) && (p1 != null)) return 1
        return 0
    }
}

class MyComparatorThumbsDown : Comparator<UrbanDictionaryDefinition> {
    override fun compare(p0: UrbanDictionaryDefinition?, p1: UrbanDictionaryDefinition?): Int {
        if ((p0 != null) && (p1 != null)) {
            if (p0.thumbs_down > p1.thumbs_down) return -1
            else if (p0.thumbs_down < p1.thumbs_down) return 1
            return 0
        }
        if ((p0 != null) && (p1 == null)) return -1
        if ((p0 == null) && (p1 != null)) return 1
        return 0
    }
}
