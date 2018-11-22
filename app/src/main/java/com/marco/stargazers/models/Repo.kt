package com.marco.stargazers.models

import com.google.gson.annotations.SerializedName

data class Repo(val name: String,
                val description: String?,
                val language: String?,
                @SerializedName("stargazers_count")
                val stargazers : Number)