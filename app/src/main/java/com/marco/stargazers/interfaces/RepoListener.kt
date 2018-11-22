package com.marco.stargazers.interfaces

import com.marco.stargazers.models.service.Repo

/**
 * Created by marco on 22/11/2018
 */

interface RepoListener {

    fun onRepoSelected(repo : Repo)

}