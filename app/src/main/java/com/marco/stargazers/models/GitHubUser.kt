package com.marco.stargazers.models

import com.google.gson.annotations.SerializedName

/**
 * Created by marco on 23/11/2018
 */
data class GitHubUser(
    @SerializedName("login") val name : String,
    @SerializedName("avatar_url") val imageUrl : String)