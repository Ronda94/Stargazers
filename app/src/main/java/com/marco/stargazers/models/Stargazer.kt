package com.marco.stargazers.models

import com.google.gson.annotations.SerializedName

data class Stargazer(
    @SerializedName("login") val username: String,
    @SerializedName("avatar_url") val imageUrl: String)