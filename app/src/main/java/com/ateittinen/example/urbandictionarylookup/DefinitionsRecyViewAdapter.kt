package com.ateittinen.example.urbandictionarylookup

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ateittinen.example.urbandictionarylookup.domain.AllUrbanDictionaryDefinitions

/**
 * Author: Anna Teittinen
 * Date: July 27, 2020
 */
class SingleDefinitionRecyViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val singleDefinitionLayout = view
    val textViewThumbsUpCount = singleDefinitionLayout.findViewById<TextView>(R.id.textView_thumbsUpCount)
    val textViewThumbsDownCount = singleDefinitionLayout.findViewById<TextView>(R.id.textView_thumbsDownCount)
    val textViewDesc = singleDefinitionLayout.findViewById<TextView>(R.id.textView_desc)
}

class DefinitionsRecyViewAdapter
    : RecyclerView.Adapter<SingleDefinitionRecyViewHolder>() {

    private var sortByThumbsUp: Boolean = true
    private var definitions : AllUrbanDictionaryDefinitions = AllUrbanDictionaryDefinitions()


    fun updateSortByThumbsUp(sortByThumbsUp: Boolean) {
        this.sortByThumbsUp = sortByThumbsUp
    }

    fun updateDefinitions(allUrbanDictionaryDefinitions: AllUrbanDictionaryDefinitions) {
        definitions = allUrbanDictionaryDefinitions
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SingleDefinitionRecyViewHolder {
        return SingleDefinitionRecyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.single_definition, parent, false))
    }

    override fun getItemCount(): Int = definitions.list.size

    override fun onBindViewHolder(holder: SingleDefinitionRecyViewHolder, position: Int) {
        val urbanDictionaryDefinition = definitions.list.get(position)
        holder.textViewThumbsUpCount.text = urbanDictionaryDefinition.thumbs_up.toString()
        holder.textViewThumbsDownCount.text = urbanDictionaryDefinition.thumbs_down.toString()
        holder.textViewDesc.text = urbanDictionaryDefinition.definition
    }
}