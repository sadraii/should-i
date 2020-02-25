/*
* Copyright 2020 Mostafa Sadraii
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.sadraii.shouldi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.PictureFirebaseDataStore
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.dao.UserFirebaseDataStore
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.entity.UserEntity
import com.sadraii.shouldi.data.repository.PictureRepository
import com.sadraii.shouldi.data.repository.UserRepository
import kotlinx.coroutines.launch

class VoteViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao: UserDao
    private val userRepo: UserRepository
    private val pictureDao: PictureDao
    private val pictureRepo: PictureRepository

    internal lateinit var user: FirebaseUser
    internal var isAuthenticating = false

    private val _currentVotePicture: MutableLiveData<PictureEntity?> by lazy {
        MutableLiveData<PictureEntity?>()
    }
    internal val currentVotePicture: LiveData<PictureEntity?> = _currentVotePicture

    private val _currentVoteUser: MutableLiveData<UserEntity?> by lazy {
        MutableLiveData<UserEntity?>()
    }
    internal val currentVoteUser: LiveData<UserEntity?> = _currentVoteUser

    init {
        val db = ShouldIDatabase.getDatabase(application, viewModelScope)
        userDao = db.userDao()
        userRepo = UserRepository(userDao, UserFirebaseDataStore())
        pictureDao = db.pictureDao()
        pictureRepo = PictureRepository(pictureDao, PictureFirebaseDataStore())
    }

    internal fun addUser(user: FirebaseUser) {
        this.user = user
        val userToAdd = with(user) {
            UserEntity(
                uid,
                displayName,
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

    internal fun voteYes(vote: Boolean) {
        viewModelScope.launch {
            userRepo.updateLastVote(user, currentVotePicture.value!!)
            pictureRepo.updatePictureVoteCount(currentVotePicture.value!!, vote)
        }
        updateCurrentPicture()
    }

    internal fun updateCurrentPicture() {
        viewModelScope.launch {
            val pictureEntity = userRepo.nextPictureToVote(user)
            val userEntity = userRepo.getUser(pictureEntity?.userId)
            _currentVotePicture.postValue(pictureEntity)
            _currentVoteUser.postValue(userEntity)
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






