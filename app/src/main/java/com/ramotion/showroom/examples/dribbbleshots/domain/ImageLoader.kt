package com.ramotion.showroom.examples.dribbbleshots.domain

import android.widget.ImageView

interface ImageLoader {

  fun loadImage(iv: ImageView, url: String, centerCrop: Boolean, cornerRadius: Int = 0)
}