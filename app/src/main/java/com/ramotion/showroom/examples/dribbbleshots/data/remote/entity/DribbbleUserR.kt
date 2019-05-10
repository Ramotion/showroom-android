package com.ramotion.showroom.examples.dribbbleshots.data.remote.entity

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
data class DribbbleUserR(
    @JsonField(name = ["id"]) var id: Int? = 0,
    @JsonField(name = ["name"]) var name: String? = "",
    @JsonField(name = ["avatar_url"]) var avatarUrl: String? = ""
)