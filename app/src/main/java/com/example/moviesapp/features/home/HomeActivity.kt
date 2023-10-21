package com.example.moviesapp.features.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moviesapp.*
import com.example.moviesapp.adapters.CategoryAdapter
import com.example.moviesapp.databinding.ActivityHomeBinding
import com.example.moviesapp.features.details.DetailsActivity
import com.example.moviesapp.subFeatures.movies.DetailsStarter
import com.example.moviesapp.subFeatures.movies.TopBarFragment

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