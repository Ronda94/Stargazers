package com.marco.stargazers.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Repo(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("stargazers_count") val stargazers : Number,
    @SerializedName("owner") val owner: Owner
    ) : Parcelable



@Parcelize
data class Owner(
    @SerializedName("avatar_url") val imageUrl : String,
    @SerializedName("login") val name : String) : Parcelable