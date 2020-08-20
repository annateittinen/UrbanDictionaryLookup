package com.ateittinen.example.urbandictionarylookup.presentation

import com.ateittinen.example.urbandictionarylookup.domain.AllUrbanDictionaryDefinitions
import com.ateittinen.example.urbandictionarylookup.domain.LookupUseCase
import com.ww.roxie.BaseViewModel
import com.ww.roxie.Reducer
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.ofType
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
class LookupViewModel(
    initialState: State?,
    private val lookupUseCase: LookupUseCase
) : BaseViewModel<Action, State>() {
    override val initialState = initialState ?: State.Idle

    private var term: String = ""
    private var sortByThumbsUp: Boolean = true
    private var allUrbanDictionaryDefinitions: AllUrbanDictionaryDefinitions = AllUrbanDictionaryDefinitions()

    private val reducer: Reducer<State, Change> = { state, change ->
        when (change) {
            is Change.Loading -> {
                term = change.term
                State.Loading(term)
            }
            is Change.Loaded -> {
                allUrbanDictionaryDefinitions = change.allUrbanDictionaryDefinitions
                State.Loaded(term, allUrbanDictionaryDefinitions)
            }
            is Change.LoadError -> {
                allUrbanDictionaryDefinitions = AllUrbanDictionaryDefinitions()
                State.LoadingError(term, change.throwable)
            }

            is Change.Sorting -> {
                sortByThumbsUp = change.thumbsUp
                State.Sorting(sortByThumbsUp)
            }
            is Change.Sorted -> {
                allUrbanDictionaryDefinitions = change.allUrbanDictionaryDefinitions
                State.Sorted(sortByThumbsUp, allUrbanDictionaryDefinitions)
            }
        }
    }

    init {
        bindActions()
    }

    private fun bindActions() {
        val loadObservable = actions.ofType<Action.Load>()
            .switchMap {
                term = it.term
                Single.fromCallable {
                    lookupUseCase.getDefinitions(sortByThumbsUp, term)
                }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .toObservable()
                    .map<Change> {
                        //No need to call Single.toFuture().get()
                        //Single.blockingGet() can be used instead; blocking will not happen since the
                        //result has already been returned at this point in the stream.
                        allUrbanDictionaryDefinitions = it.blockingGet().allUrbanDictionaryDefinitions!!
                        Change.Loaded(term, allUrbanDictionaryDefinitions)
                    }
                    .onErrorReturn {
                        Change.LoadError(term, it)
                    }
                    .startWith(Change.Loading(term))
            }

        val sortObservable = actions.ofType<Action.Sort>()
            .switchMap {
                sortByThumbsUp = it.thumbsUp
                lookupUseCase.sort(sortByThumbsUp, allUrbanDictionaryDefinitions)
                    .subscribeOn(Schedulers.io())
                    .toObservable()
                    .map<Change> {
                        allUrbanDictionaryDefinitions = it
                        Change.Sorted(sortByThumbsUp, allUrbanDictionaryDefinitions)
                    }
                    .startWith(Change.Sorting(sortByThumbsUp))
            }

        val sortByCurrentObservable = actions.ofType<Action.SortByCurrent>()
            .switchMap {
                lookupUseCase.sort(sortByThumbsUp, allUrbanDictionaryDefinitions)
                    .subscribeOn(Schedulers.io())
                    .toObservable()
                    .map<Change> {
                        allUrbanDictionaryDefinitions = it
                        Change.Sorted(sortByThumbsUp, allUrbanDictionaryDefinitions)
                    }
                    .startWith(Change.Sorting(sortByThumbsUp))
            }

        val allChanges = Observable.merge(loadObservable, sortObservable, sortByCurrentObservable)

        disposables += allChanges
            .scan(initialState, reducer)
            .filter {
                it != State.Idle
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(state::setValue)
    }

}