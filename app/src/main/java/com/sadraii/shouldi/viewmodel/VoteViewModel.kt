package com.sadraii.shouldi.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.dao.UserFirebaseDataSource
import com.sadraii.shouldi.data.entity.UserEntity
import com.sadraii.shouldi.data.repository.UserRepository
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class VoteViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao: UserDao // TODO(remove)
    private val userRepo: UserRepository

    internal var isAuthenticating = false

    init {
        val db = ShouldIDatabase.getDatabase(application, viewModelScope)
        userDao = db.userDao()
        userRepo = UserRepository(userDao, UserFirebaseDataSource())

        viewModelScope.launch {
            userDao.insert(
                UserEntity(
                    UUID.randomUUID().toString(),
                    "55",
                    "55",
                    "55",
                    "55",
                    Instant.now().toEpochMilli(),
                    null
                )
            )
            Log.d(TAG, "inserted 55")
        }
    }

    internal fun addUser(user: FirebaseUser) {
        val userToAdd = with(user) {
            UserEntity(
                uid,
                displayName,
                "First", /* TODO Get first/last name */
                "Last",
                email,
                metadata!!.creationTimestamp,
                metadata!!.lastSignInTimestamp,
                user.photoUrl
            )
        }
        viewModelScope.launch {
            userRepo.addOrUpdate(userToAdd)
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            // TODO Test if this is called by viewModels()
            if (modelClass.isAssignableFrom(VoteViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return VoteViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct VoteViewModel")
        }
    }
}


