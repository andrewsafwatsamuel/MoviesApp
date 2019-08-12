package com.example.moviesapp.features.credits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.CreditsResponse
import com.example.moviesapp.R
import com.example.moviesapp.adapters.CreditsAdapter
import com.example.moviesapp.drawCredits
import com.example.moviesapp.features.details.EXTRA_CREDITS
import com.example.moviesapp.subFeatures.movies.MoviesFragment
import kotlinx.android.synthetic.main.activity_details.*

class CreditsActivity : AppCompatActivity() {

    private val fragment by lazy { credits_fragment as MoviesFragment }

    private val recyclerView by lazy {
        fragment.view?.findViewById<RecyclerView>(R.id.movies_recycler_view)
    }

    private val creditsResponse by lazy {intent.getSerializableExtra(EXTRA_CREDITS) as CreditsResponse }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_credits)

        fragment.onConnectivityCheck()

        with(recyclerView!!) {
            layoutManager = LinearLayoutManager(this@CreditsActivity)
            adapter = CreditsAdapter(R.layout.credits_vertical_card, drawCredits(creditsResponse,true))
        }
    }
}