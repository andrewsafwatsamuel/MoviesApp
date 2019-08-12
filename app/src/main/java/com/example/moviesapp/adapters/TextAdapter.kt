package com.example.moviesapp.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R

abstract class TextViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    protected val textItem by lazy { view.findViewById<TextView>(R.id.item_text_view)!! }
    open fun bind(string: String) {
        textItem.text = string
    }
}

abstract class TextListAdapter<I : TextViewHolder>(
    private val strings: LiveData<List<String>>,
    lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<I>() {
    init {
        strings.observe(lifecycleOwner, Observer { notifyDataSetChanged() })
    }

    protected fun inflateView(parent: ViewGroup) =
        LayoutInflater.from(parent.context).inflate(R.layout.text_item, parent, false)!!

    override fun getItemCount() = strings.value?.size ?: 0

    override fun onBindViewHolder(holder: I, position: Int) = holder.bind(strings.value?.get(position) ?: "")

}

class GenreViewHolder(view: View) : TextViewHolder(view)
class GenreAdapter(
    genres: MutableLiveData<List<String>>,
    lifecycleOwner: LifecycleOwner
) : TextListAdapter<GenreViewHolder>(genres, lifecycleOwner) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        GenreViewHolder(inflateView(parent))
}

const val ACTION_SEARCH = "com.example.moviesapp.features.details.action.search"
const val EXTRA_KEY = "com.example.moviesapp.features.details.extra.key"

class RecentSearchHolder(private val view: View) : TextViewHolder(view) {
    override fun bind(string: String) {
        super.bind(string)
        textItem.setOnClickListener { onClick(string) }
    }

    private fun onClick(string: String) =
        Intent(ACTION_SEARCH)
            .putExtra(EXTRA_KEY, string)
            .also { view.context.sendBroadcast(it) }
}

class RecentSearchesAdapter(
    searches: MutableLiveData<List<String>>,
    lifecycleOwner: LifecycleOwner
) : TextListAdapter<RecentSearchHolder>(searches, lifecycleOwner) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        inflateView(parent)
            .apply { background = context.getDrawable(android.R.color.transparent) }
            .let { RecentSearchHolder(it) }
}