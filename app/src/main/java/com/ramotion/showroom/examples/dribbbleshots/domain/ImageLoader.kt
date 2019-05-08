package com.ramotion.showroom.examples.dribbbleshots.domain

import android.widget.ImageView

interface ImageLoader {

  fun loadImage(
      iv: ImageView,
      url: String,
      centerCrop: Boolean = false,
      cornerRadius: Int = 0,
      withAnim: Boolean = false,
      cache: Boolean = true,
      asGif:Boolean = false)
}