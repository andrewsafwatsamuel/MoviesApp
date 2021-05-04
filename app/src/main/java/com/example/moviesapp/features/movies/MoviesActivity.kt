package com.example.moviesapp.features.movies

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.MovieResponse
import com.example.domain.useCases.Error
import com.example.domain.useCases.Loading
import com.example.domain.useCases.MovieParams
import com.example.domain.useCases.MovieState
import com.example.domain.useCases.Success
import com.example.moviesapp.*
import com.example.moviesapp.databinding.ActivityMoviesBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MoviesActivity : AppCompatActivity() {

    private val params by lazy { MovieParams("popular", 1, "") }

    private val viewModel by lazy {
        ViewModelProvider(
            this,
            MoviesViewModelFactory(params, checkConnectivity())
        )[MoviesViewModel::class.java]
    }

    private lateinit var binding: ActivityMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.state.observe(this, Observer(::observerOnStates))
    }

    private fun observerOnStates(state: MovieState) = when (state) {
        is Loading -> onLoading()
        is Success -> onSuccess(state.response)
        is Error -> onError()
    }

    private fun onLoading() {
        Log.d(MoviesActivity::class.simpleName, "loading")
    }

    private fun onSuccess(data: MovieResponse) {
        lifecycleScope.launch {
            data.createMoviePager(data.pageCount) {
                viewModel.getMovies(true, params.copy(pageNumber = it))
            }.collectLatest {
Log.d("MovieActivity",it.toString())
            }
        }
    }

    private fun onError() {
        Log.d(MoviesActivity::class.simpleName, "error")
    }

}