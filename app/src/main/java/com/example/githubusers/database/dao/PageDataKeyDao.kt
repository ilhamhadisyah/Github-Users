package com.example.githubusers.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubusers.models.PageDataKey

@Dao
interface PageDataKeyDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePageDataKey(pageDataKey: PageDataKey)

    @Query("SELECT * FROM page_data_key ORDER BY id DESC")
    suspend fun getPageDataKey(): PageDataKey

    @Query("DELETE FROM page_data_key")
    suspend fun clearPageDataKey()
}
