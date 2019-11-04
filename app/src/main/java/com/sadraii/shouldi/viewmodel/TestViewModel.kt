package com.sadraii.shouldi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.database.ShouldIDatabase
import com.sadraii.shouldi.data.entity.UserEntity
import kotlinx.coroutines.launch
import java.util.Date

class TestViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    // LiveData gives us updated words when they change.

    val dao: UserDao

    init {
        Log.d(TAG, "calling getDatabase()")
        dao = ShouldIDatabase.getDatabase(application, viewModelScope).userDao()
        viewModelScope.launch {
            dao.insert(UserEntity(55, "55", "55", "55", "55", Date(), null))
            Log.d(TAG, "inserted 55")
        }
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on the mainthread, blocking
     * the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called viewModelScope which we
     * can use here.
     */
}