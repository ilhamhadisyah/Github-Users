package com.example.githubusers.network

import com.example.githubusers.models.UserDetailResponse
import com.example.githubusers.models.UserResponse
import com.example.githubusers.models.UserResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("users")
    suspend fun getUsers(
        @Query("since")
        since: Int?
    ): List<UserResponseItem>

    @GET("users/{username}")
    suspend fun getUserDetail(
        @Path("username")
        userLogin: String?
    ): Response<UserDetailResponse>
}