package com.sadraii.shouldi.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.entity.UserEntity
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.UUID

class CaptionViewModel(application: Application) : AndroidViewModel(application) {

    private val dao: UserDao
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private val storage = Firebase.storage(ShouldIDatabase.GS_BUCKET)

    init {
        dao = ShouldIDatabase.getDatabase(application, viewModelScope).userDao()
        viewModelScope.launch {
            dao.insert(UserEntity(UUID.randomUUID().toString(), "55", "55", "55", "55", Instant.now(), null))
            Log.d(TAG, "inserted 55")
        }
    }

    internal fun addPicture(picture: Bitmap) {
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(CaptionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return CaptionViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct CaptionViewModel")
        }
    }
}

