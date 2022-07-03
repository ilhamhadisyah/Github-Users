package com.example.githubusers.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import com.example.githubusers.data.DataRepositories
import com.example.githubusers.models.UserDetailResponse
import com.example.githubusers.models.UserResponseItem
import com.example.githubusers.network.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@DelicateCoroutinesApi
@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class UserDetailViewModel @Inject constructor(private val dataRepositories: DataRepositories) : ViewModel() {
    private val data = MutableLiveData<Resource<UserDetailResponse>>()

    fun getDetailUser(login : String) : LiveData<Resource<UserDetailResponse>> {

        GlobalScope.launch {
            data.postValue( dataRepositories.getDetailUser(login))
        }
        return data
    }

    fun updateUserDetail(userDetail: UserDetailResponse){
        GlobalScope.launch {
            dataRepositories.updateUserDetail(userDetail)
        }
    }
}