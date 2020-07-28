package com.ateittinen.example.urbandictionarylookup.presentation

import com.ww.roxie.BaseAction

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
sealed class Action: BaseAction {
    data class Load(val term: String): Action()
    data class Sort(val thumbsUp: Boolean): Action()
    object SortByCurrent: Action()
}