package com.marco.stargazers.models.service

data class Repo(val name: String,
                val description: String,
                val language: String? = null,
                val stargazers : Int)