package com.example.moviesapp.features.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.CreditsMember
import com.example.CreditsResponse
import com.example.CrewMember
import com.example.DetailsResponse
import com.example.moviesapp.*
import com.example.moviesapp.adapters.CreditsAdapter
import com.example.moviesapp.adapters.GenreAdapter
import com.example.moviesapp.adapters.GridAdapter
import com.example.moviesapp.adapters.ID_EXTRA
import com.example.moviesapp.databinding.ActivityDetailsBinding
import com.example.moviesapp.databinding.FragmentCreditsBinding
import com.example.moviesapp.features.credits.CreditsActivity
import com.example.moviesapp.features.trailer.TrailerActivity
import com.example.moviesapp.subFeatures.movies.*

const val EXTRA_CREDITS = "com.example.moviesapp.features.details.extraCredits"
const val EXTRA_TRAILER = "com.example.moviesapp.features.details.extraTrailer"

class DetailsActivity : AppCompatActivity() {

    private var binding: ActivityDetailsBinding? = null
    private val viewModel by lazy { ViewModelProviders.of(this)[DetailsViewModel::class.java] }
    private val creditsFragment by lazy { supportFragmentManager.findFragmentById(R.id.credits_fragment) as CreditsFragment }
    private val moviesFragment by lazy { supportFragmentManager.findFragmentById(R.id.related_movies_fragment) as HorizontalMovieFragment }
    private val parameters = MutableLiveData<QueryParameters<Unit>>()
    private val movieAdapter by lazy {
        GridAdapter(
            viewModel.movieList,
            R.layout.horizontal_movie_item
        )
    }
    private val topBarFragment by lazy { supportFragmentManager.findFragmentById(R.id.top_bar_fragment) as TopBarFragment }
    private val genresLayoutManager = LinearLayoutManager(
        this@DetailsActivity, LinearLayoutManager.HORIZONTAL, false
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        DetailsStarter(this, DetailsActivity::class.java, true)
        viewModel.relatedResult.observe(this) {
            parameters.value = QueryParameters(it.pageNumber, it.pageCount, Unit)
            movieAdapter.addItems(it.results)
            onMoviesRetrieved()
        }
        binding?.emptyStateLayout?.apply {
            viewModel.errorLiveData.observe(this@DetailsActivity) { setErrorState(it) }
            retrieveData(onConnectivityCheck(this))
            reload(this) { retrieveData(it);viewModel.errorLiveData.value = null }
        }
        drawDetails()
        drawCredits()
        drawRelated()
    }

    private fun onMoviesRetrieved() {
        binding?.relatedMoviesContainer?.visibility =
            if (viewModel.movieList.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun retrieveData(connected: Boolean) = with(viewModel) {
        setId(intent.getLongExtra(ID_EXTRA, 0))
        retrieveCredits(connected)
        retrieveDetails(connected)
        retrieveRelated(connected)
    }


    private fun drawDetails() = viewModel.detailsResult.observe(this) {
        binding?.apply {
            drawPhoto(POSTER_SIZE, it.poster, detailsPosterImageView)
            drawPhoto(BACK_DRAW_SIZE, it.backDropPhoto, coverImageView)
            adultTextView.visibility = if (it.isAdult == true) View.VISIBLE else View.GONE
            ratingTextView.text = "${it.voteAverage}"
            viewModel.genres.value = it.genres?.map { g -> g.name ?: "" }
            genresRecyclerView
                .apply { layoutManager = genresLayoutManager }
                .apply { adapter = GenreAdapter(viewModel.genres, this@DetailsActivity) }
            releaseDateTextView.text =
                setDetailsText(R.string.released_in, it.releaseDate ?: "-/-/-")
            durationTextView.text = setDetailsText(R.string.play_time, "${it.runTime} min")
            overviewTextView.text = it.overView
            drawTopBar(it.title)
            getTrailer(it) { id -> startTrailer(id) }
        }
    }

    private fun drawTopBar(title: String?) = with(topBarFragment) {
        activityTitle(title ?: "")
        searchButton().visibility = View.GONE
        backButton().setOnClickListener { finish() }
    }

    private inline fun getTrailer(response: DetailsResponse, trailer: (String) -> Unit) =
        response.trailers?.videos
            ?.takeUnless { it.isEmpty() }
            ?.run { trailer(get(0)?.key ?: "") }

    private fun startTrailer(trailerId: String) =
        binding?.playButton?.apply { visibility = View.VISIBLE }
            ?.setOnClickListener {
                Intent(this, TrailerActivity::class.java)
                    .apply { putExtra(EXTRA_TRAILER, trailerId) }
                    .let { startActivity(it) }
            }

    private fun setDetailsText(resource: Int, text: String) = "${getString(resource)} $text"

    private fun drawCredits() = viewModel.creditsResult.observe(this) {
        with(creditsFragment) {
            addCast(drawCredits(it, false))
            setDirector(getMembers(it) { it?.job.equals("Director") })
            setWriters(getMembers(it) { it?.department.equals("Writing") })
            openCreditsScreen(it)
        }
    }

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

    private val relatedLayoutMAnager = LinearLayoutManager(
        this@DetailsActivity,
        LinearLayoutManager.HORIZONTAL,
        false
    )

    private fun drawRelated() = moviesFragment
        .apply { showMore()?.visibility = View.GONE }
        .apply { setCategory(getString(R.string.you_might_like)) }
        .recyclerView()
        ?.apply { layoutManager = relatedLayoutMAnager }
        ?.apply { adapter = movieAdapter }
        ?.addOnScrollListener(pagination(relatedLayoutMAnager))

    private fun pagination(manager: LinearLayoutManager) = PaginationScrollListener.Builder<Unit>()
        .layoutManager(manager)
        .lifecycleOwner(this)
        .queryParameters(parameters)
        .retrieve { queryParams ->
            binding?.emptyStateLayout?.let { emptyStateLayoutBinding ->
                viewModel.retrieveRelated(
                    onConnectivityCheck(emptyStateLayoutBinding),
                    queryParams.pageNumber + 1
                )
            }
        }.build()

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}

class CreditsFragment : Fragment() {
    private var binding: FragmentCreditsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreditsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    fun addCast(cast: List<CreditsMember>): RecyclerView? = binding?.castRecyclerView
        ?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        ?.apply { adapter = CreditsAdapter(R.layout.credits_horizonal_card, cast) }

    fun setDirector(director: String) {
        binding?.directorTextView?.text = director
    }

    fun setWriters(writers: String) {
        binding?.writersTextView?.text = writers
    }

    fun openCreditsScreen(credits: CreditsResponse) = Intent(context, CreditsActivity::class.java)
        .apply { putExtra(EXTRA_CREDITS, credits) }
        .also { intent -> binding?.creditsDetailsTextView?.setOnClickListener { startActivity(intent) } }
}