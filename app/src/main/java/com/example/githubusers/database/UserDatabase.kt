package com.example.githubusers.database

import androidx.room.Database
import androidx.room.Insert
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.githubusers.database.dao.PageDataKeyDao
import com.example.githubusers.database.dao.UserListDao
import com.example.githubusers.di.ApplicationScope
import com.example.githubusers.models.PageDataKey
import com.example.githubusers.models.UserDetailResponse
import com.example.githubusers.models.UserResponseItem
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

@Database(
    entities = [UserResponseItem::class, UserDetailResponse::class, PageDataKey::class],
    version = 4,
    exportSchema = false
)
abstract class UserDatabase : RoomDatabase() {

    abstract fun pageDataKeyDao(): PageDataKeyDao
    abstract fun userListDao(): UserListDao

    class CallBack @Inject constructor(@ApplicationScope private val applicationScope: CoroutineScope) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
        }
    }
}