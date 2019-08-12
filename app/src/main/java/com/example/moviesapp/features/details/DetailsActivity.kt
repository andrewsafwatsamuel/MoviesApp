package com.example.moviesapp.features.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.CreditsMember
import com.example.CreditsResponse
import com.example.CrewMember
import com.example.moviesapp.*
import com.example.moviesapp.adapters.CreditsAdapter
import com.example.moviesapp.adapters.GenreAdapter
import com.example.moviesapp.adapters.GridAdapter
import com.example.moviesapp.adapters.ID_EXTRA
import com.example.moviesapp.features.credits.CreditsActivity
import com.example.moviesapp.subFeatures.movies.MoviesFragment
import com.example.moviesapp.subFeatures.movies.PaginationScrollListener
import com.example.moviesapp.subFeatures.movies.QueryParameters
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.fragment_credits.*
import kotlinx.android.synthetic.main.no_internet_connection.*


class DetailsActivity : AppCompatActivity() {

    private val viewModel by lazy { ViewModelProviders.of(this)[DetailsViewModel::class.java] }
    private val creditsFragment by lazy { credits_fragment as CreditsFragment }
    private val moviesFragment by lazy {
        (related_movies_fragment as MoviesFragment).apply { close = true }
    }
    private val parameters = MutableLiveData<QueryParameters<Unit>>()
    private val adapter by lazy { GridAdapter(viewModel.movieList, R.layout.horizontal_list_item) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        not_connected_layout.visibility = if (checkConnectivity(this)) View.GONE else View.VISIBLE
        viewModel.relatedResult.observe(this, Observer {
            parameters.value = QueryParameters(it.pageNumber, it.pageCount, Unit)
            adapter.addItems(it.results)
            onMoviesRetrieved()
        })
        retrieveData(checkConnectivity(this))
        drawDetails()
        drawCredits()
        drawRelated()
    }

    private fun onMoviesRetrieved() {
        you_may_like.visibility = if (viewModel.movieList.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun retrieveData(connected: Boolean) = with(viewModel) {
        setId(intent.getLongExtra(ID_EXTRA, 0))
        retrieveCredits(connected)
        retrieveDetails(connected)
        retrieveRelated(connected)
    }

    private fun drawDetails() = viewModel.detailsResult.observe(this, Observer {
        drawPhoto(POSTER_SIZE, it.poster, details_poster_image_view)
        drawPhoto(BACK_DRAW_SIZE, it.backDropPhoto, cover_image_view)
        adult_text_view.visibility = if (it.isAdult == true) View.VISIBLE else View.GONE
        rating_text_view.text = "${it.voteAverage}"
        viewModel.genres.value = it.genres?.map { g -> g.name ?: "" }
        genres_recycler_view
            .apply { layoutManager = LinearLayoutManager(this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false) }
            .apply { adapter = GenreAdapter(viewModel.genres, this@DetailsActivity) }
        release_date_text_view.text = setText(R.string.released_in, it.releaseDate ?: "-/-/-/-")
        revenue_text_view.text = setText(R.string.revenue, "${it.revenue}$")
        duration_text_view.text = setText(R.string.play_time, "${it.runTime} min")
        overview_text_view.text = it.overView
        println(it)
    })

    private fun setText(resource: Int, text: String) = "${getString(resource)} $text"

    private fun drawCredits() = viewModel.creditsResult.observe(this, Observer {
        with(creditsFragment) {
            addCast(drawCredits(it, false))
            setDirector(getMembers(it) { it?.job.equals("Director") })
            setWriters(getMembers(it) { it?.department.equals("Writing") })
            openCreditsScreen()
        }
    })

    private fun getMembers(
        response: CreditsResponse,
        filter: (CrewMember?) -> Boolean
    ) = response.crew.asSequence()
        .filter { filter(it) }
        .map { it?.name }
        .toList()
        .toString()
        .removePrefix("[")
        .removeSuffix("]")

    private val manager = LinearLayoutManager(
        this@DetailsActivity,
        LinearLayoutManager.HORIZONTAL,
        false
    )

    private fun drawRelated() = moviesFragment.drawRecycler(
        manager,
        adapter,
        pagination(manager)
    )

    private fun pagination(manager: LinearLayoutManager) = PaginationScrollListener.Builder<Unit>()
        .layoutManager(manager)
        .lifecycleOwner(this)
        .queryParameters(parameters)
        .retrieve { viewModel.retrieveRelated(checkConnectivity(this), it.pageNumber + 1) }
        .build()
}

class CreditsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_credits, container, false)

    fun addCast(cast: List<CreditsMember>) = cast_recycler_view
        .apply { layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false) }
        .apply { adapter = CreditsAdapter(R.layout.credits_horizonal_card, cast) }

    fun setDirector(director: String) {
        director_text_view.text = director
    }

    fun setWriters(writers: String) {
        writers_text_view.text = writers
    }

    fun openCreditsScreen() = Intent(context, CreditsActivity::class.java)
        .also { /*data to be viewed in credits*/ }
        .also { intent ->
            credits_details_text_view.setOnClickListener { startActivity(intent) }
        }
}