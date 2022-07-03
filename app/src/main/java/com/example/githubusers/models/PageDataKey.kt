package com.example.githubusers.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_data_key")
data class PageDataKey(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val nextPage: Int? = null,
    val prevPage: Int? = null
)