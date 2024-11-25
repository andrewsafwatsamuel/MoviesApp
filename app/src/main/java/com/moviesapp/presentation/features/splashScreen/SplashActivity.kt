package com.moviesapp.presentation.features.splashScreen

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moviesapp.presentation.R
import com.moviesapp.presentation.features.home.HomeActivity
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

        Observable.timer(1700, TimeUnit.MILLISECONDS, Schedulers.io())
            .subscribe { startSearchScreen() }
            .also { disposable.addAll() }
    }

    private fun startSearchScreen() {
        Intent(this, HomeActivity::class.java)
            .also { startActivity(it) }
            .also { finish() }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}