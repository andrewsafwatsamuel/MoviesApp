package com.moviesapp.presentation.features.credits

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.moviesapp.CreditsResponse
import com.moviesapp.presentation.R
import com.moviesapp.presentation.adapters.CreditsAdapter
import com.moviesapp.presentation.databinding.ActivityCreditsBinding
import com.moviesapp.presentation.drawCredits
import com.moviesapp.presentation.features.details.EXTRA_CREDITS
import com.moviesapp.presentation.onConnectivityCheck
import com.moviesapp.presentation.subFeatures.movies.MoviesFragment
import com.moviesapp.presentation.subFeatures.movies.TopBarFragment

class CreditsActivity : AppCompatActivity() {

    private var binding: ActivityCreditsBinding? = null
    private val fragment by lazy { supportFragmentManager.findFragmentById(R.id.credits_fragment) as MoviesFragment }

    private val topBarFragment by lazy { supportFragmentManager.findFragmentById(R.id.top_bar_fragment) as TopBarFragment }

    private val recyclerView by lazy {
        fragment.view?.findViewById<RecyclerView>(R.id.movies_recycler_view)
    }

    private val creditsResponse by lazy { intent.getSerializableExtra(EXTRA_CREDITS) as CreditsResponse }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreditsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        drawTopFragment()
        binding?.emptyStateLayout?.run {
            onConnectivityCheck(this)
            reloadTextView.setOnClickListener { onConnectivityCheck(this) }
        }
        drawCredits()
    }

    private fun drawTopFragment() = with(topBarFragment) {
        activityTitle("Credits")
        backButton().setOnClickListener { finish() }
        searchButton().visibility = View.GONE
    }

    private fun drawCredits() = with(recyclerView!!) {
        layoutManager = LinearLayoutManager(this@CreditsActivity)
        adapter = CreditsAdapter(R.layout.credits_vertical_card, drawCredits(creditsResponse, true))
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}