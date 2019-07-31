package com.example.moviesapp

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.CreditsMember
import com.example.CreditsResponse

const val BASE_POSTER_URL = "http://image.tmdb.org/t/p/"
const val POSTER_SIZE = "w185"
const val BACK_DRAW_SIZE = "w500"

fun drawPhoto(size: String, url: String?, view: ImageView) = url
    .takeUnless { it.isNullOrBlank() }
    ?.let { Glide.with(view).load("$BASE_POSTER_URL$size$it")}
    ?.placeholder(R.drawable.movie_roll)
    ?.error(R.drawable.movie_roll)
    ?.into(view)?: view.setImageResource(R.drawable.movie_roll)

fun checkConnectivity(activity: Activity): Boolean =
    (activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
        .run { activeNetworkInfo != null && activeNetworkInfo.isConnected }

interface BaseTextWatcher:TextWatcher{
    override fun afterTextChanged(s: Editable)=Unit

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) = Unit
}
fun hideKeyboard(activity: Activity) = activity.getSystemService(Activity.INPUT_METHOD_SERVICE)
    .let { it as InputMethodManager }
    .also { it.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0) }
fun pageCount(count:Int)=if (count<=60) count else 60

fun drawRecycler(it: CreditsResponse, withCrew: Boolean) = mutableListOf<CreditsMember>()
    .apply { it.cast.forEach { i -> add(CreditsMember(i?.name, i?.character, i?.photo)) } }
    .apply { if (withCrew) it.crew.forEach { i -> add(CreditsMember(i?.name, i?.job, i?.photo)) } }
