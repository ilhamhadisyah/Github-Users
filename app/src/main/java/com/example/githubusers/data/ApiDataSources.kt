package com.example.githubusers.data

import com.example.githubusers.network.ApiService
import javax.inject.Inject

class ApiDataSources @Inject constructor(private val apiService: ApiService) {

    suspend fun getUsers(since: Int) = apiService.getUsers(since)

    suspend fun getUserDetail(login: String) = apiService.getUserDetail(login)
}