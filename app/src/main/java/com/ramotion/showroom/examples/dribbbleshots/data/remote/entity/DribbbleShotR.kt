package com.ramotion.showroom.examples.dribbbleshots.data.remote.entity

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
data class DribbbleShotR(
    @JsonField(name = ["title"]) var title: String? = "",
    @JsonField(name = ["html_url"]) var htmlUrl: String? = "",
    @JsonField(name = ["images"]) var images: DribbbleShotRImage? = DribbbleShotRImage(),
    @JsonField(name = ["id"]) var id: Int? = 0,
    @JsonField(name = ["animated"]) var animated: Boolean? = false
)

@JsonObject
data class DribbbleShotRImage(
    @JsonField(name = ["normal"]) var normal: String? = "",
    @JsonField(name = ["teaser"]) var teaser: String? = ""
)