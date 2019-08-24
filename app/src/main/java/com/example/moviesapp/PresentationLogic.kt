package com.example.moviesapp

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.CreditsMember
import com.example.CreditsResponse
import kotlinx.android.synthetic.main.empty_state_layout.*

const val BASE_POSTER_URL = "http://image.tmdb.org/t/p/"
const val POSTER_SIZE = "w185"
const val BACK_DRAW_SIZE = "w500"
const val ERROR_MESSAGE="Error has occurred while loading"

fun drawPhoto(size: String, url: String?, view: ImageView) = url
    .takeUnless { it.isNullOrBlank() }
    ?.let { Glide.with(view).load("$BASE_POSTER_URL$size$it") }
    ?.placeholder(R.drawable.movie_roll)
    ?.error(R.drawable.movie_roll)
    ?.into(view) ?: view.setImageResource(R.drawable.movie_roll)


interface BaseTextWatcher : TextWatcher {
    override fun afterTextChanged(s: Editable) = Unit

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
}

fun hideKeyboard(activity: Activity) = activity.getSystemService(Activity.INPUT_METHOD_SERVICE)
    .let { it as InputMethodManager }
    .also { it.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0) }

fun pageCount(count: Int) = if (count <= 60) count else 60

fun drawCredits(response: CreditsResponse, withCrew: Boolean) = mutableListOf<CreditsMember>()
    .apply { response.cast.forEach { i -> add(CreditsMember(i?.name, i?.character, i?.photo)) } }
    .apply {
        if (withCrew) response.crew.forEach { i ->
            add(
                CreditsMember(
                    i?.name,
                    i?.job,
                    i?.photo
                )
            )
        }
    }


fun Activity.onConnectivityCheck(): Boolean = checkConnectivity()
    .also { empty_state_layout.visibility = if (it) View.GONE else View.VISIBLE }
    .also { empty_state_imageView.setImageDrawable(getDrawable(R.drawable.ic_wifi_black_24dp)) }

fun Activity.checkConnectivity(): Boolean =
    (getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        .run { activeNetworkInfo != null && activeNetworkInfo.isConnected }

fun Activity.setErrorState(message: String?) = message
    .takeUnless { it.isNullOrBlank() }
    ?.also { empty_state_textView.text = it }
    ?.let { empty_state_imageView.setImageDrawable(getDrawable(R.drawable.error)) }
    ?.also { empty_state_layout.visibility=View.VISIBLE }

fun Activity.reload(isConnected: (Boolean) -> Unit) =
    reload_Text_view.setOnClickListener { isConnected(onConnectivityCheck()) }