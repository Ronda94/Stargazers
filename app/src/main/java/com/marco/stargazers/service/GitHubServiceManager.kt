package com.marco.stargazers.service

import com.marco.stargazers.models.GitHubUser
import com.marco.stargazers.models.Repo
import retrofit2.Call


private val gitHubService by lazy {
    GitHubService.newInstance()
}

fun listRepos(username: String, page: Int) : Call<List<Repo>> {
        return gitHubService.listRepos(username,page)
}

fun listStargazers(ownerName: String, repoName: String, page: Int): Call<List<GitHubUser>>{
    return gitHubService.listStargazers(ownerName, repoName, page)
}