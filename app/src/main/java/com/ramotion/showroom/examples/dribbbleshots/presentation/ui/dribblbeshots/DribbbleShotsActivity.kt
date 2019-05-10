package com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ramotion.showroom.R
import com.ramotion.showroom.examples.dribbbleshots.domain.ImageLoader
import com.ramotion.showroom.examples.dribbbleshots.utils.BaseView
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.View.*
import android.view.WindowManager
import com.jakewharton.rxbinding2.support.v7.widget.RxToolbar
import com.ramotion.showroom.databinding.ActivityDribbbleShotsBinding
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots.DribbbleShotsIntent.*
import java.util.concurrent.TimeUnit
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots.ShotsListItem.*

class DribbbleShotsActivity : AppCompatActivity(), BaseView<DribbbleShotsState> {

  private var binding: ActivityDribbbleShotsBinding? = null
  private val intentsPublisher = PublishSubject.create<DribbbleShotsIntent>()
  private lateinit var intentsSubscription: Disposable
  private val viewModel: DribbbleShotsViewModel by viewModel()
  private lateinit var rvShotsAdapter: DribbbleShotsAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    handleStates()
    initBinding()
    initRvDribbbleShots()
    initIntents()
  }

  private fun initBinding() {
    binding = DataBindingUtil.setContentView(this, R.layout.activity_dribbble_shots)
  }

  private fun initRvDribbbleShots() {
    with(binding!!.rvDribbbleShots) {
      //Adapter
      val imageLoader: ImageLoader = get()
      rvShotsAdapter = DribbbleShotsAdapter(imageLoader)
      adapter = rvShotsAdapter

      layoutManager = GridLayoutManager(
          this@DribbbleShotsActivity,
          2,
          RecyclerView.VERTICAL,
          false
      ).apply {

        //LayoutManager
        spanSizeLookup = object : SpanSizeLookup() {
          override fun getSpanSize(position: Int): Int {
            val type = rvShotsAdapter.getItemViewType(position)
            return if (type == SHOTS_LOADING_ITEM || type == DRIBBBLE_PLACEHOLDER_ITEM) 2 else 1
          }
        }

        //ScrollListener
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
          override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val glm = layoutManager as GridLayoutManager
            val totalItemCount = rvShotsAdapter.itemCount
            val lastVisibleItem = glm.findLastVisibleItemPosition()
            if (!rvShotsAdapter.currentList.contains(ShotsLoadingItem) &&
                !rvShotsAdapter.currentList.contains(ShotsLoadingItem) &&
                totalItemCount % 2 == 0 &&
                totalItemCount <= lastVisibleItem + 4 &&
                dy > 0
            ) {
              val nextPage = (totalItemCount / 20) + 1
              intentsPublisher.onNext(GetNextDribbbleShotsPage(nextPage))
            }
          }
        })

        //ItemDecoration
        addItemDecoration(DribbbleShotsItemDecoration(this@DribbbleShotsActivity))
      }
    }
  }

  override fun onDestroy() {
    binding = null
    intentsSubscription.dispose()
    super.onDestroy()
  }

  override fun initIntents() {
    intentsSubscription = Observable.merge(listOf(
        intentsPublisher,

        RxToolbar.navigationClicks(binding!!.tbDribbbleShots)
            .throttleFirst(1000, TimeUnit.MILLISECONDS)
            .doOnNext { onBackPressed() }
    ))
        .subscribe(viewModel.viewIntentsConsumer())
  }

  override fun handleStates() {
    viewModel.stateReceived().observe(this, Observer { state -> render(state) })
  }

  override fun render(state: DribbbleShotsState) {
    binding!!.pbInitLoading.visibility = if (state.loading) VISIBLE else GONE

    rvShotsAdapter.submitList(state.shots)

    state.error?.run {
      Snackbar.make(
          binding!!.root,
          message ?: getString(R.string.error_occurred),
          Snackbar.LENGTH_SHORT)
          .show()
    }
  }
}
