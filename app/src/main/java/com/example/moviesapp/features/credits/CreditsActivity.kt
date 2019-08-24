package com.example.moviesapp.features.credits

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.CreditsResponse
import com.example.moviesapp.R
import com.example.moviesapp.adapters.CreditsAdapter
import com.example.moviesapp.drawCredits
import com.example.moviesapp.features.details.EXTRA_CREDITS
import com.example.moviesapp.onConnectivityCheck
import com.example.moviesapp.subFeatures.movies.MoviesFragment
import com.example.moviesapp.subFeatures.movies.TopBarFragment
import kotlinx.android.synthetic.main.activity_credits.*
import kotlinx.android.synthetic.main.activity_details.credits_fragment
import kotlinx.android.synthetic.main.empty_state_layout.*

class CreditsActivity : AppCompatActivity() {

    private val fragment by lazy { credits_fragment as MoviesFragment }

    private val topBarFragment by lazy { top_bar_fragment as TopBarFragment }

    private val recyclerView by lazy {
        fragment.view?.findViewById<RecyclerView>(R.id.movies_recycler_view)
    }

    private val creditsResponse by lazy { intent.getSerializableExtra(EXTRA_CREDITS) as CreditsResponse }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)

        drawTopFragment()
        onConnectivityCheck()
        reload_Text_view.setOnClickListener { onConnectivityCheck() }
        drawCredits()
    }

    private fun drawTopFragment()=with(topBarFragment) {
        activityTitle("Credits")
        backButton().setOnClickListener { finish() }
        searchButton().visibility = View.GONE
    }

    private fun drawCredits()= with(recyclerView!!) {
        layoutManager = LinearLayoutManager(this@CreditsActivity)
        adapter = CreditsAdapter(R.layout.credits_vertical_card, drawCredits(creditsResponse, true))
    }
}