package com.example.moviesapp.features.home.movies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.Movie
import com.example.domain.useCases.MoviesUseCase
import com.example.moviesapp.createPager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.lang.IllegalStateException
import kotlin.coroutines.CoroutineContext

class MoviesViewModel(
    category: String,
    pager: Pager<Int, Movie> = createMoviesPager(category),
    val movieFlow: MutableStateFlow<PagingData<Movie>> = MutableStateFlow(PagingData.empty()),
    viewModelContext:CoroutineContext = Dispatchers.IO
) : ViewModel() {
    init {
        viewModelScope.launch(viewModelContext) { pager.flow.cachedIn(viewModelScope).collectLatest {movieFlow.emit(it) } }
    }
}

fun createMoviesPager(
    category: String,
    useCase: MoviesUseCase = MoviesUseCase()
) = createPager(20,{
    useCase(category,it.key?:1)
})

@Suppress("UNCHECKED_CAST")
class MoviesViewModelFactory(
    private val category: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(MoviesViewModel::class.java)) MoviesViewModel(category) as T
        else throw IllegalStateException("Bad ViewModel class")
}