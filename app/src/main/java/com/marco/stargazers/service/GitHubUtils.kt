package com.marco.stargazers.service

import com.marco.stargazers.models.Repo
import retrofit2.Call


private val gitHubService by lazy {
    GitHubService.newInstance()
}

fun listRepos(username: String, page: Int) : Call<List<Repo>> {
        return gitHubService.listRepos(username,page)
}