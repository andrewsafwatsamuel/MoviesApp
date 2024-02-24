package com.moviesapp.presentation.features.trailer

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.moviesapp.presentation.databinding.DialogTrailerBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class TrailerDialog(private val trailerId: String) : DialogFragment() {

    private var binding: DialogTrailerBinding? = null
    private val youtubePlayerListener = object : AbstractYouTubePlayerListener() {
        override fun onReady(youTubePlayer: YouTubePlayer) {
            youTubePlayer.loadVideo(trailerId, 0f)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window!!.attributes.apply {
            gravity = Gravity.FILL_HORIZONTAL or Gravity.TOP
        }

        binding = DialogTrailerBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.youtubePlayerView
            ?.also(lifecycle::addObserver)
            ?.apply { addYouTubePlayerListener(youtubePlayerListener) }
    }

    override fun onDestroyView() {
        binding?.youtubePlayerView?.removeYouTubePlayerListener(youtubePlayerListener)
        binding = null
        super.onDestroyView()
    }

    companion object {
        const val TRAILER_DIALOG_TAG = "TrailerDialog"
    }
}