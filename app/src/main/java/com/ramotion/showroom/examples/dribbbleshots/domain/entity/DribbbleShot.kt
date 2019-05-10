package com.ramotion.showroom.examples.dribbbleshots.domain.entity

data class DribbbleShot(
    var title: String = "",
    var htmlUrl: String = "",
    var oneX: String = "",
    var imageTeaser: String = "",
    var imageNormal: String = "",
    var imageHi: String = "",
    var id: Int = 0,
    var userId: Int = 0,
    var message: String = "",
    var saved: Boolean = false) {

  companion object {
    val EMPTY = DribbbleShot()
  }
}