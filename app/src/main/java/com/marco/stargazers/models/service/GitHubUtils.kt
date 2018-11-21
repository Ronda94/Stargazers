package com.marco.stargazers.models.service

import retrofit2.Call


private val gitHubService by lazy {
    GitHubService.newInstance()
}

fun listRepos(owner : String) : Call<List<Repo>> {
        return gitHubService.listRepos(owner)
}