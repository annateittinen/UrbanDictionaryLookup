package com.ateittinen.example.urbandictionarylookup.data

import com.ateittinen.example.urbandictionarylookup.domain.AllUrbanDictionaryDefinitions
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val BASE_URL ="https://mashape-community-urban-dictionary.p.rapidapi.com"
const val HEADERS_HOST = "x-rapidapi-host: mashape-community-urban-dictionary.p.rapidapi.com"
const val HEADERS_KEY= "x-rapidapi-key: 2d8a978f4amsha54b7275fd9da65p1c5affjsn1bfa3f2532a2"
//const val HEADERS_KEY= "x-rapidapi-key: SIGN-UP-FOR-KEY"
const val HEADERS_USEQUERYSTRING = "useQueryString: true"

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
interface UrbanDictinaryInterface {
    @Headers(
        HEADERS_HOST,
        HEADERS_KEY,
        HEADERS_USEQUERYSTRING
    )
    @GET("/define")
    open fun getDefinition(
        @Query("term") term: String
    ): Call<AllUrbanDictionaryDefinitions>
}

class LookupRepository {
    fun getDefinitions(term: String) : LookupResponse {
        val kotlinJsonAdapterFactory =  KotlinJsonAdapterFactory()
        val moshi = Moshi.Builder().add(kotlinJsonAdapterFactory).build()
        val moshiConverterFactory = MoshiConverterFactory.create(moshi)

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .build()

        val urbanDictinaryInterface = retrofit.create(UrbanDictinaryInterface::class.java)
        val call = urbanDictinaryInterface.getDefinition(
            term)
        val response : Response<AllUrbanDictionaryDefinitions> = call.execute()

        return LookupResponse(response)
    }
}