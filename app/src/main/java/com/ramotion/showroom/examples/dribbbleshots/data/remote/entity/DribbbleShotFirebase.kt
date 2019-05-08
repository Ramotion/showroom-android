package com.ramotion.showroom.examples.dribbbleshots.data.remote.entity

data class DribbbleShotFirebase(
    var date: Long? = 0,
    var title: String? = "",
    val html_url: String? = "",
    var id: Int? = 0,
    var userId: Int? = 0,
    var message: String? = "")