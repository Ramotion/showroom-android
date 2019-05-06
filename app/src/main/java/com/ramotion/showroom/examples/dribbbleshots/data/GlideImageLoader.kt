package com.ramotion.showroom.examples.dribbbleshots.data

import android.widget.ImageView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import com.ramotion.showroom.examples.dribbbleshots.domain.ImageLoader

object GlideImageLoader : ImageLoader {

  override fun loadImage(iv: ImageView, url: String, centerCrop: Boolean, cornerRadius: Int, withAnim: Boolean, cache: Boolean) {
    val options = RequestOptions().apply {
      when {
        centerCrop && cornerRadius > 0 -> transforms(CenterCrop(), RoundedCorners(cornerRadius))
        centerCrop -> centerCrop()
        cornerRadius > 0 -> transform(RoundedCorners(cornerRadius))
      }
    }

    var request = GlideApp.with(iv)
        .load(url)

    if (withAnim) {
      request = request.transition(withCrossFade())
    }

    request = if (cache) {
      request.diskCacheStrategy(DiskCacheStrategy.ALL)
    } else {
      request.diskCacheStrategy(DiskCacheStrategy.NONE)
    }

    request.apply(options)
        .into(iv)
  }
}