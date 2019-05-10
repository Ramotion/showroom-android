package com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.*
import com.jakewharton.rxbinding2.view.RxView
import com.ramotion.showroom.R
import com.ramotion.showroom.databinding.ItemDribbbleShotBinding
import com.ramotion.showroom.databinding.ItemDribbbleShotsEmptyPlaceholderBinding
import com.ramotion.showroom.databinding.ItemLoadingDribbbleShotBinding
import com.ramotion.showroom.examples.dribbbleshots.domain.ImageLoader
import com.ramotion.showroom.examples.dribbbleshots.domain.entity.DribbbleShot
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribbbledetails.DribbbleShotDetailsActivity
import com.ramotion.showroom.examples.dribbbleshots.presentation.ui.dribblbeshots.ShotsListItem.*
import java.util.concurrent.TimeUnit

const val SHOTS_LOADING_ITEM = 0x999
const val DRIBBBLE_SHOT_ITEM = 0x998
const val DRIBBBLE_PLACEHOLDER_ITEM = 0x997

private const val UPDATE_SAVED = 0x996
private const val UPDATE_IMAGE = 0x995

class DribbbleShotsAdapter(private val imageLoader: ImageLoader)
  : ListAdapter<ShotsListItem, ViewHolder>(object : DiffUtil.ItemCallback<ShotsListItem>() {

  override fun areItemsTheSame(oldItem: ShotsListItem, newItem: ShotsListItem): Boolean =
    when {
      oldItem is ShotsLoadingItem && newItem is ShotsLoadingItem -> true
      oldItem is DribbblePlaceholder && newItem is DribbblePlaceholder -> true
      oldItem is DribbbleShotItem && newItem is DribbbleShotItem && oldItem.shot.id == newItem.shot.id -> true
      else -> false
    }

  override fun areContentsTheSame(oldItem: ShotsListItem, newItem: ShotsListItem): Boolean =
    when {
      oldItem is ShotsLoadingItem && newItem is ShotsLoadingItem -> true
      oldItem is DribbblePlaceholder && newItem is DribbblePlaceholder -> true
      oldItem is DribbbleShotItem && newItem is DribbbleShotItem && oldItem.shot == newItem.shot -> true
      else -> false
    }

  override fun getChangePayload(oldItem: ShotsListItem, newItem: ShotsListItem): Any? =
    if (oldItem is DribbbleShotItem && newItem is DribbbleShotItem) {
      when {
        oldItem.shot.saved != newItem.shot.saved -> UPDATE_SAVED
        oldItem.shot.imageNormal != newItem.shot.imageNormal -> UPDATE_IMAGE
        else -> Any()
      }
    } else
      Any()
}) {

  override fun getItemViewType(position: Int): Int {
    val item = getItem(position)

    return when (item) {
      is ShotsLoadingItem -> SHOTS_LOADING_ITEM
      is DribbbleShotItem -> DRIBBBLE_SHOT_ITEM
      is DribbblePlaceholder -> DRIBBBLE_PLACEHOLDER_ITEM
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
    when (viewType) {
      DRIBBBLE_SHOT_ITEM -> DribbbleShotVH(
          DataBindingUtil.inflate(LayoutInflater.from(parent.context),
              R.layout.item_dribbble_shot,
              parent,
              false
          ))
      SHOTS_LOADING_ITEM -> ShotsLoadingVH(
          DataBindingUtil.inflate(LayoutInflater.from(parent.context),
              R.layout.item_loading_dribbble_shot,
              parent,
              false
          ))
      else -> DribbblePlaceholderVH(
          DataBindingUtil.inflate(LayoutInflater.from(parent.context),
              R.layout.item_dribbble_shots_empty_placeholder,
              parent,
              false
          ))
    }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val item = getItem(position)
    if (holder is DribbbleShotVH && item is DribbbleShotItem) {
      holder.bind(item.shot, imageLoader)
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
    val item = getItem(position)
    if (holder is DribbbleShotVH && item is DribbbleShotItem) {
      if (payloads.isEmpty()) {
        holder.bind(item.shot, imageLoader)
      } else {
        for (payload in payloads) {
          when (payload) {
            UPDATE_SAVED -> holder.updateSaved(item.shot)
            UPDATE_IMAGE -> holder.updateImage(item.shot, imageLoader)
          }
        }
      }
    }
  }
}


class ShotsLoadingVH(binding: ItemLoadingDribbbleShotBinding) : ViewHolder(binding.root)


class DribbbleShotVH(private val binding: ItemDribbbleShotBinding) : ViewHolder(binding.root) {
  fun bind(shot: DribbbleShot, imageLoader: ImageLoader) {
    binding.maskSaved.visibility = if (shot.saved) VISIBLE else GONE
    binding.ivGif.visibility = VISIBLE
    imageLoader.loadImage(
        iv = binding.ivShot,
        url = shot.imageNormal,
        centerCrop = true,
        cornerRadius = 20,
        withAnim = true
    )

    if (!shot.saved) {
      RxView.clicks(binding.dribbbleShotContainer)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .doOnNext {
            val intent = Intent(binding.root.context, DribbbleShotDetailsActivity::class.java).apply {
              putExtra("shotId", shot.id)
            }
            binding.root.context.startActivity(intent)
          }
          .subscribe()
    }
  }

  fun updateSaved(shot: DribbbleShot) {
    binding.maskSaved.visibility = if (shot.saved) VISIBLE else GONE
    if (!shot.saved) {
      RxView.clicks(binding.dribbbleShotContainer)
          .throttleFirst(1000, TimeUnit.MILLISECONDS)
          .doOnNext {
            val intent = Intent(binding.root.context, DribbbleShotDetailsActivity::class.java).apply {
              putExtra("shotId", shot.id)
            }
            binding.root.context.startActivity(intent)
          }
          .subscribe()
    } else {
      binding.dribbbleShotContainer.isClickable = false
    }
  }

  fun updateImage(shot: DribbbleShot, imageLoader: ImageLoader) {
    imageLoader.loadImage(
        iv = binding.ivShot,
        url = shot.imageNormal,
        centerCrop = true,
        cornerRadius = 20,
        withAnim = true
    )
  }
}


class DribbblePlaceholderVH(binding: ItemDribbbleShotsEmptyPlaceholderBinding) : ViewHolder(binding.root)