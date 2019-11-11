package com.sadraii.shouldi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.entity.UserEntity
import kotlinx.coroutines.launch
import java.util.Date

class VoteViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: UserDao // TODO(remove)
    internal var isAuthenticating = false

    init {
        // TODO(remove)
        Log.d(TAG, "calling getDatabase()")
        dao = ShouldIDatabase.getDatabase(application, viewModelScope).userDao()
        viewModelScope.launch {
            dao.insert(UserEntity(55, "55", "55", "55", "55", Date(), null))
            Log.d(TAG, "inserted 55")
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(VoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return VoteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct VoteViewModel")
        }
    }
}