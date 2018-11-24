package com.marco.stargazers.service

import com.marco.stargazers.models.GitHubUser
import com.marco.stargazers.models.Repo
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface GitHubService {

    @GET("users/{username}/repos")
    fun listRepos(@Path("username") username: String, @Query("page") page: Int) : Call<List<Repo>>

    @GET("/repos/{owner}/{repo}/stargazers")
    fun listStargazers(@Path("owner") owner: String, @Path("repo") repo: String, @Query("page") page: Int) : Call<List<GitHubUser>>

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