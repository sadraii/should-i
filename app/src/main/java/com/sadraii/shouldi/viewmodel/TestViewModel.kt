package com.sadraii.shouldi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.entity.UserEntity
import kotlinx.coroutines.launch
import java.util.Date

class TestViewModel(application: Application) : AndroidViewModel(application) {

    val dao: UserDao

    init {
        Log.d(TAG, "calling getDatabase()")
        dao = ShouldIDatabase.getDatabase(application, viewModelScope).userDao()
        viewModelScope.launch {
            dao.insert(UserEntity(55, "55", "55", "55", "55", Date(), null))
            Log.d(TAG, "inserted 55")
        }
    }
}