package com.example.moviesapp.features.movies

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviesapp.databinding.ActivityMoviesBinding
import com.example.moviesapp.subFeatures.movies.MoviesScreen

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMoviesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoviesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MoviesScreen(this, this, binding)
    }

}