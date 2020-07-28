package com.ateittinen.example.urbandictionarylookup.data

import com.ateittinen.example.urbandictionarylookup.domain.AllUrbanDictionaryDefinitions
import retrofit2.Response


/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
class LookupResponse(response: Response<AllUrbanDictionaryDefinitions>) {
    val code : Int = response.code() // 200..300 -> OK
    val isSuccessful : Boolean = response.isSuccessful
    val message: String = response.message() //eg. "Internal Server Error"
    val errorBody = response.errorBody().toString() //eg. "Oops, an error occurred. Please contact support@rapi…]"
    val allUrbanDictionaryDefinitions: AllUrbanDictionaryDefinitions? = response.body()
}