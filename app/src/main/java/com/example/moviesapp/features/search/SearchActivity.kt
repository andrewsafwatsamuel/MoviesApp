package com.example.moviesapp.features.search

import android.app.Activity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.subFeatures.movies.MoviesFragment
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    private val viewModel
            by lazy { ViewModelProviders.of(this)[SearchViewModel::class.java] }
    private val moviesFragment
            by lazy { movies_fragment as MoviesFragment }
    private val recyclerView
            by lazy { moviesFragment.view?.findViewById<RecyclerView>(R.id.movies_recycler_view)!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        supportActionBar?.hide()

    }

    private fun hideKeyboard() = this.getSystemService(Activity.INPUT_METHOD_SERVICE)
        .let { it as InputMethodManager }
        .also { it.hideSoftInputFromWindow(currentFocus?.windowToken, 0) }
}