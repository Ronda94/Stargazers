package com.marco.stargazers.models

import com.google.gson.annotations.SerializedName

data class Repo(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("stargazers_count") val stargazers : Number)