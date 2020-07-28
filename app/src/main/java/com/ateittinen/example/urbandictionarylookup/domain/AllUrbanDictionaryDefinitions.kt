package com.ateittinen.example.urbandictionarylookup.domain

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
data class AllUrbanDictionaryDefinitions (
    val list: List<UrbanDictionaryDefinition> = listOf()
)

data class UrbanDictionaryDefinition(
    val definition: String,
    //val permalink: String?,
    val thumbs_up: Long,
    //val sound_urls: List<String>?,
    //val author: String?,
    //val word: String?,
    //val defid: Long,
    //val current_vote: String?,
    //val written_on: String,
    //val example: String?,
    val thumbs_down: Long
)
