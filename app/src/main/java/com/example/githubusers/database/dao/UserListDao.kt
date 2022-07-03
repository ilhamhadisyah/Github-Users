package com.example.githubusers.database.dao

import androidx.paging.PagingSource
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import androidx.room.OnConflictStrategy.REPLACE
import com.example.githubusers.models.UserDetailResponse
import com.example.githubusers.models.UserResponseItem

@Dao
interface UserListDao {

    @Insert(onConflict = IGNORE)
    suspend fun saveUserList(userList : List<UserResponseItem>)

    @Query("SELECT * FROM user_item")
    fun getUserItemList():PagingSource<Int,UserResponseItem>

    @Query("DELETE FROM user_item")
    suspend fun deleteUserItem()

    @Update
    suspend fun updateItem(user : UserResponseItem)

    @Query("UPDATE user_item SET hasNotes = :note WHERE id = :userId")
    suspend fun changeNoteStatus(userId : Int, note : Boolean)


    @Insert(onConflict = IGNORE)
    suspend fun saveUserDetail(userDetail : UserDetailResponse)

    @Query("SELECT * FROM user_detail WHERE login = :userLogin")
    fun getUserDetail(userLogin : String) : UserDetailResponse

    @Query("DELETE FROM user_detail WHERE login = :userLogin")
    suspend fun deleteUser(userLogin : String)

    @Update
    suspend fun updateUser(userDetail: UserDetailResponse)

}