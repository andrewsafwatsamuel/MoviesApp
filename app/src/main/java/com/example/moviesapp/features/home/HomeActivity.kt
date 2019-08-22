package com.example.moviesapp.features.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.R
import com.example.moviesapp.adapters.CategoryAdapter
import com.example.moviesapp.checkConnectivity
import com.example.moviesapp.features.details.DetailsActivity
import com.example.moviesapp.onConnectivityCheck
import com.example.moviesapp.reload
import com.example.moviesapp.subFeatures.movies.DetailsStarter
import com.example.moviesapp.subFeatures.movies.TopBarFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[HomeViewModel::class.java] }

    private val topFragment by lazy { home_top_bar_fragment as TopBarFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        viewModel.loadingLiveData.observe(this, Observer {
            home_progress_bar.visibility = if (it == true) View.VISIBLE else View.GONE
        })

        viewModel.errorLiveData.observe(this, Observer {
            if (!it.isNullOrBlank()) onError(it) else onSuccess()
        })

        DetailsStarter(this, DetailsActivity::class.java)
        drawTopBar()
        retrieveData(onConnectivityCheck())
        reload { retrieveData(it) }
        drawList()
    }

    private fun onError(message: String)= error_text_view
        .apply { text = message}
        .apply { visibility = View.VISIBLE  }
        .let { home_recyclerView.visibility=View.GONE }

    private fun onSuccess(){
        error_text_view.visibility=View.GONE
        home_recyclerView.visibility=View.VISIBLE
    }

    private fun drawTopBar() = with(topFragment) {
        backButton().visibility = View.GONE
        this.activityTitle(getString(R.string.home))
    }

    private fun retrieveData(connected: Boolean) = with(viewModel) {
        loadSingleCategory(connected, "popular")
        loadSingleCategory(connected, "upcoming")
        loadSingleCategory(connected, "now_playing")
        loadSingleCategory(connected, "top_rated")
    }

    private fun drawList() = with(home_recyclerView) {
        layoutManager = LinearLayoutManager(this@HomeActivity)
        adapter = CategoryAdapter(this@HomeActivity, viewModel.resultLiveData)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.result.clear()
        viewModel.errorLiveData.value = null
    }
}