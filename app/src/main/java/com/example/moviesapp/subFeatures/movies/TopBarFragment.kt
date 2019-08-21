package com.example.moviesapp.subFeatures.movies

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.moviesapp.R
import com.example.moviesapp.features.search.SearchActivity
import kotlinx.android.synthetic.main.top_bar.*

class TopBarFragment : Fragment() {

    fun backButton() = view!!.findViewById<ImageView>(R.id.back_button)

    fun searchButton() = view!!.findViewById<ImageView>(R.id.search_image_view)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.top_bar, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchButton()?.setOnClickListener { startSearchScreen() }
    }

    private fun startSearchScreen() = Intent(context!!, SearchActivity::class.java)
        .also { startActivity(it) }

    fun activityTitle(title: String) {
        activity_title_text_view.text = title
    }
}