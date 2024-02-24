package com.moviesapp.presentation.features.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.moviesapp.presentation.R
import com.moviesapp.presentation.adapters.CategoryAdapter
import com.moviesapp.presentation.databinding.ActivityHomeBinding
import com.moviesapp.presentation.features.details.DetailsActivity
import com.moviesapp.presentation.onConnectivityCheck
import com.moviesapp.presentation.reload
import com.moviesapp.presentation.setErrorState
import com.moviesapp.presentation.subFeatures.movies.DetailsStarter
import com.moviesapp.presentation.subFeatures.movies.TopBarFragment

class HomeActivity : AppCompatActivity() {

    private var binding: ActivityHomeBinding? = null
    private val viewModel by lazy { ViewModelProviders.of(this)[HomeViewModel::class.java] }
    private val topFragment by lazy { supportFragmentManager.findFragmentById(R.id.home_top_bar_fragment) as TopBarFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        viewModel.loadingLiveData.observe(this) {
            binding?.homeProgressBar?.visibility = if (it == true) View.VISIBLE else View.GONE
        }

        viewModel.errorLiveData.observe(this) { binding?.emptyStateLayout?.setErrorState(it) }

        DetailsStarter(this, DetailsActivity::class.java)
        drawTopBar()
        binding?.emptyStateLayout?.let {
            retrieveData(onConnectivityCheck(it))
            reload(it, ::retrieveData)
        }
        drawList()
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

    private fun drawList() = binding?.homeRecyclerView?.run {
        layoutManager = LinearLayoutManager(this@HomeActivity)
        adapter = CategoryAdapter(this@HomeActivity, viewModel.resultLiveData)
    }

    override fun onDestroy() {
        viewModel.result.clear()
        viewModel.errorLiveData.value = null
        binding = null
        super.onDestroy()
    }
}