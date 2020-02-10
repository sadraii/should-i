package com.sadraii.shouldi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.dao.UserFirebaseDataStore
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.entity.UserEntity
import com.sadraii.shouldi.data.repository.UserRepository
import kotlinx.coroutines.launch

class VoteViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao: UserDao // TODO(remove)
    private val userRepo: UserRepository
    internal lateinit var user: FirebaseUser
    internal val currentPicture: MutableLiveData<PictureEntity> by lazy {
        MutableLiveData<PictureEntity>()
    }

    internal var isAuthenticating = false

    init {
        // TODO remove this
        val db = ShouldIDatabase.getDatabase(application, viewModelScope)
        userDao = db.userDao()
        userRepo = UserRepository(userDao, UserFirebaseDataStore())
        //
        // viewModelScope.launch {
        //     userDao.insert(
        //         UserEntity(
        //             UUID.randomUUID().toString(),
        //             "55",
        //             "55",
        //             "55",
        //             "55",
        //             Instant.now().toEpochMilli(),
        //             null
        //         )
        //     )
        //     Log.d(TAG, "inserted 55")
        // }
    }

    internal fun addUser(user: FirebaseUser) {
        this.user = user
        val userToAdd = with(user) {
            UserEntity(
                uid,
                displayName,
                "First", /* TODO Get first/last name */
                "Last",
                email,
                metadata!!.creationTimestamp,
                metadata!!.lastSignInTimestamp,
                photoUrl
            )
        }
        viewModelScope.launch {
            userRepo.addOrUpdate(userToAdd)
        }
    }

    internal fun updateCurrentPicture() {
        viewModelScope.launch {
            userRepo.nextPictureToVote(user)?.let { nextPic ->
                currentPicture.postValue(nextPic)
            }
        }
    }

    internal fun voteYes() {
    }

    internal fun voteNo() {
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




