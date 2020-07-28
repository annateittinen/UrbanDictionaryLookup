package com.ateittinen.example.urbandictionarylookup

import com.ateittinen.example.urbandictionarylookup.data.LookupRepository
import com.ateittinen.example.urbandictionarylookup.data.LookupResponse
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testLookupRepository() {
        val lookupResponse : LookupResponse = LookupRepository().getDefinitions("lol")
        lookupResponse.allUrbanDictionaryDefinitions?.list?.forEach {
            println("thumbsup=" + it.thumbs_up + " thumbsdown=" + it.thumbs_down + " def=" + it.definition)
        }
    }
}