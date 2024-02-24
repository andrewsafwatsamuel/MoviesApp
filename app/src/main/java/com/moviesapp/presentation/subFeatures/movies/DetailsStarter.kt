package com.moviesapp.presentation.subFeatures.movies

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.moviesapp.presentation.adapters.ACTION_OPEN_DETAILS_SCREEN
import com.moviesapp.presentation.adapters.ID_EXTRA
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class DetailsStarter(
    private var activity: FragmentActivity?,
    private val cls: Class<*>,
    private val isCloseable: Boolean = false,
    private val publishSubject: PublishSubject<Long> = PublishSubject.create(),
    private val disposables: CompositeDisposable = CompositeDisposable()
) : BroadcastReceiver(), LifecycleObserver {

    override fun onReceive(context: Context?, intent: Intent) =
        publishSubject.onNext(intent.getLongExtra(ID_EXTRA, 0))

    init {
        if (activity == null) throw IllegalStateException("You must pass an activity to continue")
        activity?.lifecycle?.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun startScreen() = publishSubject
        .debounce(300, TimeUnit.MILLISECONDS)
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe({ createIntent(it) }, Throwable::printStackTrace)
        .also { disposables.add(it) }

    private fun createIntent(id: Long) = Intent(activity, cls)
        .apply { putExtra(ID_EXTRA, id) }
        .let { activity!!.startActivity(it) }
        .also { if (isCloseable) activity!!.finish() }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun registerReceiver() {
        activity!!.registerReceiver(this, IntentFilter(ACTION_OPEN_DETAILS_SCREEN))
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun removeReceiver() = activity!!.unregisterReceiver(this)

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private fun clear() {
        disposables.dispose()
        activity = null
    }
}