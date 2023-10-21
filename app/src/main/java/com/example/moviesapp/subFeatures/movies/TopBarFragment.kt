package com.example.moviesapp.subFeatures.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.databinding.TopBarBinding
import com.example.moviesapp.features.search.SearchActivity

class TopBarFragment : Fragment() {

    private var binding: TopBarBinding? = null

    fun backButton() = view!!.findViewById<ImageView>(R.id.back_button)

    fun searchButton() = view!!.findViewById<ImageView>(R.id.search_image_view)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = TopBarBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchButton()?.setOnClickListener { startSearchScreen() }
    }

    private fun startSearchScreen() = Intent(context!!, SearchActivity::class.java)
        .also { startActivity(it) }

    fun activityTitle(title: String) {
        binding?.activityTitleTextView?.text = title
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}