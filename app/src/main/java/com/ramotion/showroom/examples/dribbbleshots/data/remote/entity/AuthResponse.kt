package com.ramotion.showroom.examples.dribbbleshots.data.remote.entity

import com.bluelinelabs.logansquare.annotation.JsonField
import com.bluelinelabs.logansquare.annotation.JsonObject

@JsonObject
data class AuthResponse(
    @JsonField(name = ["access_token"]) var token: String? = "",
    @JsonField(name = ["token_type"]) var tokenType: String? = "",
    @JsonField(name = ["scope"]) var scope: String? = ""
)