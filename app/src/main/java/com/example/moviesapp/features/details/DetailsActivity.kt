package com.example.moviesapp.features.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.example.moviesapp.R
import com.example.moviesapp.checkConnectivity
import com.example.moviesapp.subFeatures.movies.ID_EXTRA


class DetailsActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[DetailsViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        retrieveData(checkConnectivity(this))

    }

    private fun retrieveData(connected: Boolean) = with(viewModel) {
        setId(intent.getLongExtra(ID_EXTRA, 0))
        retrieveCredits(connected)
        retrieveDetails(connected)
        retrieveRelated(connected)
    }
}
