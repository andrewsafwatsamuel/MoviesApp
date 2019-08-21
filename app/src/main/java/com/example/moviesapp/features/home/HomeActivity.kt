package com.example.moviesapp.features.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Movie
import com.example.MovieResponse
import com.example.domain.repositories.moviesRepository
import com.example.domain.serverGateWay.moviesApi
import com.example.moviesapp.R
import com.example.moviesapp.adapters.CategoryAdapter
import com.example.moviesapp.checkConnectivity
import com.example.moviesapp.features.details.DetailsActivity
import com.example.moviesapp.subFeatures.movies.DetailsStarter
import com.example.moviesapp.subFeatures.movies.TopBarFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_home.*
import org.w3c.dom.NodeList
import retrofit2.Response
import java.util.function.Predicate
import javax.security.auth.callback.Callback

class HomeActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[HomeViewModel::class.java] }

    private val topFragment by lazy { home_top_bar_fragment as TopBarFragment }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        DetailsStarter(this, DetailsActivity::class.java)
        drawTopBar()
        retrieveData(checkConnectivity(this))
        drawList()
    }

    private fun drawTopBar()= with(topFragment){
        backButton()?.visibility= View.GONE
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
    }
}