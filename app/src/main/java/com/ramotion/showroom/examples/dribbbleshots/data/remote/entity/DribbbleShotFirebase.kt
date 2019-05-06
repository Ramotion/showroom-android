package com.ramotion.showroom.examples.dribbbleshots.data.remote.entity

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class DribbbleShotFirebase(
    var title: String? = "",
    val html_url: String? = "",
    var id: Int? = 0,
    var userId: Int? = 0,
    var message: String? = "")