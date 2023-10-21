package com.example.moviesapp.features.credits

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.CreditsResponse
import com.example.moviesapp.R
import com.example.moviesapp.adapters.CreditsAdapter
import com.example.moviesapp.databinding.ActivityCreditsBinding
import com.example.moviesapp.drawCredits
import com.example.moviesapp.features.details.EXTRA_CREDITS
import com.example.moviesapp.onConnectivityCheck
import com.example.moviesapp.subFeatures.movies.MoviesFragment
import com.example.moviesapp.subFeatures.movies.TopBarFragment

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