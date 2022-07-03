package com.example.githubusers

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GithubUsersApp : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}