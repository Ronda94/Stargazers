package com.marco.stargazers.models.service

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface GitHubService {

    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username: String): Call<List<Repo>>


    companion object {

        fun newInstance(): GitHubService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(GitHubService::class.java)
        }
    }
}

val gitHubService by lazy {
    GitHubService.newInstance()
}