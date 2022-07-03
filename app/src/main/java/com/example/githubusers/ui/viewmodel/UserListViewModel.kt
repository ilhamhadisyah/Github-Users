package com.example.githubusers.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubusers.data.DataRepositories
import com.example.githubusers.models.UserResponseItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class UserListViewModel @Inject constructor(private val dataRepositories: DataRepositories) : ViewModel() {



    private val eventChannel = Channel<Event>()
    val events = eventChannel.receiveAsFlow()

    fun getUserList() : Flow<PagingData<UserResponseItem>>{
        return dataRepositories.getUsers().cachedIn(viewModelScope)
    }

    fun updateOnClick(item : UserResponseItem){
        viewModelScope.launch {
            dataRepositories.updateAfterWatched(item)
        }
    }

    fun changeNoteStatus(userId : Int, hasNotes : Boolean){
        GlobalScope.launch {
            dataRepositories.updateNoteStatus(userId,hasNotes)
        }
    }

    sealed class Event {
        data class ShowErrorMessage(val error: Throwable) : Event()
    }
}