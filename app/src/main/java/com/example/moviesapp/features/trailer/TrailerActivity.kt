package com.example.moviesapp.features.trailer

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import com.example.moviesapp.databinding.ActivityTrailerBinding
import com.example.moviesapp.features.details.EXTRA_TRAILER
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer

private val LOG_TAG = TrailerActivity::class.java.simpleName

class TrailerActivity : YouTubeBaseActivity(), YouTubePlayer.OnInitializedListener {

    private var binding: ActivityTrailerBinding? = null

    override fun onInitializationSuccess(
        p0: YouTubePlayer.Provider?,
        p1: YouTubePlayer?,
        p2: Boolean
    ) = p1
        ?.apply { play() }
        ?.takeUnless { p2 }
        ?.run { cueVideo(intent.getStringExtra(EXTRA_TRAILER)) } ?: Unit

    override fun onInitializationFailure(
        p0: YouTubePlayer.Provider?,
        p1: YouTubeInitializationResult?
    ) {
        Log.e(LOG_TAG, p1?.toString() ?: "")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTrailerBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        setSize()
        binding?.trailerYoutubePlayer?.initialize("dummy", this)
    }

    private fun setSize() = DisplayMetrics()
        .also { windowManager.defaultDisplay.getMetrics(it) }
        .apply { window.setLayout((widthPixels * 0.95).toInt(), (heightPixels * 0.4).toInt()) }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}