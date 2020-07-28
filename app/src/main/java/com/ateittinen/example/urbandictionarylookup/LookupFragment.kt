package com.ateittinen.example.urbandictionarylookup

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.StrictMode
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ateittinen.example.urbandictionarylookup.data.LookupRepository
import com.ateittinen.example.urbandictionarylookup.domain.LookupUseCase
import com.ateittinen.example.urbandictionarylookup.presentation.Action
import com.ateittinen.example.urbandictionarylookup.presentation.LookupViewModel
import com.ateittinen.example.urbandictionarylookup.presentation.LookupViewModelFactory
import com.ateittinen.example.urbandictionarylookup.presentation.State

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
class LookupFragment : Fragment() {
    private lateinit var editTextEnterWord: EditText
    private lateinit var buttonLookup: Button
    private lateinit var buttonOrderByThumbsUp: Button
    private lateinit var buttonOrderByThumbsDown: Button
    private lateinit var viewModel: LookupViewModel
    private lateinit var textViewTermLookedup: TextView
    private lateinit var recyView: RecyclerView

    private lateinit var recyViewLayoutManager: RecyclerView.LayoutManager
    private lateinit var recyViewAdapter: DefinitionsRecyViewAdapter

    private lateinit var toast: Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set StrictMode.ThreadPolicy to catch long running code on the main thread.
        // Avoid android.os.NetworkOnMainThreadException at android.os.StrictMode$AndroidBlockGuardPolicy.onNetwork
        // This allows calls to server in LookupRepository.getDefinitions(...)
        val policy: StrictMode.ThreadPolicy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentLayout = inflater.inflate(R.layout.fragment_lookup, container, false)
        editTextEnterWord = fragmentLayout.findViewById<EditText>(R.id.editText_enterWord)
        buttonLookup = fragmentLayout.findViewById(R.id.buttonLookup)
        buttonOrderByThumbsUp = fragmentLayout.findViewById(R.id.buttonOrderByThumbsUp)
        buttonOrderByThumbsDown = fragmentLayout.findViewById(R.id.buttonOrderByThumbsDown)
        textViewTermLookedup = fragmentLayout.findViewById(R.id.textView_termLookedup)
        recyView = fragmentLayout.findViewById(R.id.recyView)
        toast = Toast.makeText(activity, "Looking up term...", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)

        buttonLookup.setOnClickListener {
            val term = editTextEnterWord.text.toString()
            if (term.length > 0) {
                viewModel.dispatch(Action.Load(term))
            }
        }

        buttonOrderByThumbsUp.setOnClickListener {
            buttonOrderByThumbsUp.background = context?.getDrawable(R.drawable.thumbs_up_selected)
            buttonOrderByThumbsDown.background = context?.getDrawable(R.drawable.thumbs_down)
            recyViewAdapter.updateSortByThumbsUp(true)
            viewModel.dispatch(Action.Sort(true))
        }

        buttonOrderByThumbsDown.setOnClickListener {
            buttonOrderByThumbsUp.background = context?.getDrawable(R.drawable.thumbs_up)
            buttonOrderByThumbsDown.background = context?.getDrawable(R.drawable.thumbs_down_selected)
            recyViewAdapter.updateSortByThumbsUp(false)
            viewModel.dispatch(Action.Sort(false))
        }

        recyViewLayoutManager = LinearLayoutManager(activity)
        recyViewAdapter = DefinitionsRecyViewAdapter()
        recyView.apply {
            layoutManager = recyViewLayoutManager
            adapter = recyViewAdapter
        }

        return fragmentLayout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this,
            LookupViewModelFactory(null, LookupUseCase(LookupRepository())))
            .get(LookupViewModel::class.java)

        viewModel.observableState.observe(this,
            Observer { state ->
                state?.let {
                    renderState(state)
                }
            }
        )
    }

    private fun renderState(state: State) {
        when (state) {
            is State.Loading -> {
                toast.show()
            }
            is State.Loaded -> {
                textViewTermLookedup.text = state.term
                recyViewAdapter.updateDefinitions(state.allUrbanDictionaryDefinitions)
                toast.cancel()
            }
            is State.LoadingError -> {
                toast.cancel()
                AlertDialog.Builder(context).setMessage(state.throwable.message)
                    .setPositiveButton("OK", DialogInterface.OnClickListener {dialog, i ->
                        dialog.cancel()
                    })
                    .create().show()
            }
            is State.Sorting -> {
                toast.show()
            }
            is State.Sorted -> {
                recyViewAdapter.updateDefinitions(state.allUrbanDictionaryDefinitions)
                toast.cancel()
            }
        }
    }
}