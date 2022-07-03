package com.example.githubusers.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.githubusers.database.UserDatabase
import com.example.githubusers.models.PageDataKey
import com.example.githubusers.models.UserResponseItem
import com.example.githubusers.utils.Constant.Companion.STARTING_SINCE_INDEX
import okio.IOException
import retrofit2.HttpException


@ExperimentalPagingApi
class RemoteDataMediator(
    private val apiDataSources: ApiDataSources,
    private val userDatabase: UserDatabase
) : RemoteMediator<Int, UserResponseItem>() {

    private val userDao = userDatabase.userListDao()
    private val pageDataKeyDao = userDatabase.pageDataKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, UserResponseItem>
    ): MediatorResult {
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> STARTING_SINCE_INDEX
                LoadType.APPEND -> pageDataKeyDao.getPageDataKey().nextPage
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            }

            val response = apiDataSources.getUsers(loadKey!!)
            val users = response.map { it }

            val nextPageSince = loadKey + 46 //when api has 'since' query, it will returned 46 object data
            val prevPageSince = if (nextPageSince == 46) 0 else loadKey - 46


            userDatabase.withTransaction {
                pageDataKeyDao.savePageDataKey(
                    PageDataKey(
                        0,
                        nextPage = nextPageSince,
                        prevPage = prevPageSince
                    )
                )
                userDao.saveUserList(users)
            }

            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }
}