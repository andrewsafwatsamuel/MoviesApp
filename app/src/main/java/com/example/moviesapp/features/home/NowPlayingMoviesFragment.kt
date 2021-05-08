package com.example.moviesapp.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.moviesapp.databinding.LayoutMoviesBinding

import com.example.moviesapp.features.home.movies.MoviesScreen

class NowPlayingMoviesFragment:Fragment() {

    private lateinit var binding: LayoutMoviesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutMoviesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MoviesScreen("now_playing",viewLifecycleOwner, this, binding)
    }
}