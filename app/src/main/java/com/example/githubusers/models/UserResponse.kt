package com.example.githubusers.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(

    @field:SerializedName("UserResponse")
    var userResponse: List<UserResponseItem>? = null
) : Parcelable

@Parcelize
@Entity(tableName = "user_item")
data class UserResponseItem(

    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("id")
    var id: Int? = null,

    @field:SerializedName("gists_url")
    var gistsUrl: String? = null,

    @field:SerializedName("repos_url")
    var reposUrl: String? = null,

    @field:SerializedName("following_url")
    var followingUrl: String? = null,

    @field:SerializedName("starred_url")
    var starredUrl: String? = null,

    @field:SerializedName("login")
    var login: String? = null,

    @field:SerializedName("followers_url")
    var followersUrl: String? = null,

    @field:SerializedName("type")
    var type: String? = null,

    @field:SerializedName("url")
    var url: String? = null,

    @field:SerializedName("subscriptions_url")
    var subscriptionsUrl: String? = null,

    @field:SerializedName("received_events_url")
    var receivedEventsUrl: String? = null,

    @field:SerializedName("avatar_url")
    var avatarUrl: String? = null,

    @field:SerializedName("events_url")
    var eventsUrl: String? = null,

    @field:SerializedName("html_url")
    var htmlUrl: String? = null,

    @field:SerializedName("site_admin")
    var siteAdmin: Boolean? = null,

    @field:SerializedName("gravatar_id")
    var gravatarId: String? = null,

    @field:SerializedName("node_id")
    var nodeId: String? = null,

    @field:SerializedName("organizations_url")
    var organizationsUrl: String? = null,

    var hasNotes: Boolean? = false,

    var isRead : Boolean? = false
) : Parcelable
