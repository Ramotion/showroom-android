package com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribbbledetails

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding2.view.RxView
import com.ramotion.showroom.R
import com.ramotion.showroom.databinding.ActivityDribbbleDetailsBinding
import com.ramotion.showroom.examples.dribbbleshots.domain.ImageLoader
import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribbbledetails.DribbbleDetailsIntent.*
import com.ramotion.showroom.examples.dribbbleshots.utils.BaseView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class DribbbleShotDetailsActivity : AppCompatActivity(), BaseView<DribbbleDetailsState> {

  private var binding: ActivityDribbbleDetailsBinding? = null
  private lateinit var intentsSubscription: Disposable
  private val viewModel: DribbbleDetailsViewModel by viewModel()
  private lateinit var currentState: DribbbleDetailsState
  private val imageLoader: ImageLoader by inject()
  private val shotId: Int by lazy(LazyThreadSafetyMode.NONE) { intent.getIntExtra("shotId", 0) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    handleStates()
    initBinding()
    initIntents()
  }

  override fun onDestroy() {
    binding = null
    super.onDestroy()
  }

  private fun initBinding() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_dribbble_details)
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(
        Observable.just(GetDribbbleShot(shotId)),

        RxView.clicks(binding!!.btnSend)
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .map { SaveDribbbleShot(currentState.shot.copy(message = binding!!.etShotMessage.text.toString().trim())) },

        RxView.clicks(binding!!.ivClose)
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            .doOnNext { onBackPressed() }
    ))
        .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: DribbbleDetailsState) {
    if (state.shotSaved) onBackPressed()

    if (state.shot != DribbbleShot.EMPTY) {
      imageLoader.loadImage(binding!!.ivShotImage, state.shot.imageNormal, false, 0, true)
    }

    binding!!.pbInitLoading.visibility = if (state.loading) VISIBLE else GONE
    binding!!.pbSaveLoading.visibility = if (state.saveLoading) VISIBLE else GONE
    binding!!.btnSend.text = if (state.saveLoading) "" else getString(R.string.send)

    state.error?.run {
      Snackbar.make(binding!!.root, message ?: getString(R.string.error_occurred), Snackbar.LENGTH_SHORT).show()
    }

    currentState = state
  }
}
