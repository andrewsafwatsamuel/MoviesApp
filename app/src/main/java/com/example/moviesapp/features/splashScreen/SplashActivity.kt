package com.example.moviesapp.features.splashScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.moviesapp.R
import com.example.moviesapp.features.search.SearchActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class SplashActivity : AppCompatActivity() {
    private val disposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()

        Observable.timer(3, TimeUnit.SECONDS, Schedulers.io())
            .subscribe { startSearchScreen() }
            .also { disposable.addAll() }
    }

    private fun startSearchScreen() {
        Intent(this, SearchActivity::class.java)
            .also { startActivity(it) }
            .also { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
