package com.example.githubusers.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.githubusers.database.UserDatabase
import com.example.githubusers.models.UserDetailResponse
import com.example.githubusers.models.UserResponseItem
import com.example.githubusers.network.BaseDataSource
import com.example.githubusers.network.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import kotlin.math.log


@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class DataRepositories @Inject constructor(
    private val apiDataSources: ApiDataSources,
    private val userDatabase: UserDatabase
) : BaseDataSource() {

    private val userDao = userDatabase.userListDao()

    /**
     * Pagination Support for offline mode
     */

    fun getUsers(): Flow<PagingData<UserResponseItem>> =
        Pager(
            PagingConfig(
                pageSize = 46,
                maxSize = 92,
                prefetchDistance = 1,
                enablePlaceholders = false
            ),
            remoteMediator = RemoteDataMediator(apiDataSources, userDatabase),
            pagingSourceFactory = { userDao.getUserItemList() }
        ).flow

    suspend fun getDetailUser(login: String): Resource<UserDetailResponse> {

        /**
         * Manual source mediator for checking whether the data is exist on database or not
         */
        return try {
            val data = userDao.getUserDetail(login)
            data.login
            Resource.success(data)

        } catch (e: NullPointerException) {
            val data = safeApiCall { apiDataSources.getUserDetail(login) }
            userDao.saveUserDetail(data.data!!)
            data
        }

    }

    suspend fun updateAfterWatched(item: UserResponseItem) {
        return userDao.updateItem(item)
    }

    suspend fun updateUserDetail(userDetail: UserDetailResponse) {
        return userDao.updateUser(userDetail)
    }

    suspend fun updateNoteStatus(userId: Int, hasNotes: Boolean) {
        return userDao.changeNoteStatus(userId, hasNotes)
    }
}