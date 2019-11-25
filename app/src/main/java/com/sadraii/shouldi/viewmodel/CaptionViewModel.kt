package com.sadraii.shouldi.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.PictureFirebaseDataSource
import com.sadraii.shouldi.data.repository.PictureRepository
import kotlinx.coroutines.launch

class CaptionViewModel(application: Application) : AndroidViewModel(application) {

    private val pictureDao: PictureDao
    private val pictureRepo: PictureRepository
    private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    private val storage = Firebase.storage(ShouldIDatabase.GS_BUCKET)

    init {
        val db = ShouldIDatabase.getDatabase(application, viewModelScope)
        pictureDao = db.pictureDao()
        pictureRepo = PictureRepository(pictureDao, PictureFirebaseDataSource())
    }

    internal fun addPicture(picture: Bitmap) {
        viewModelScope.launch {
            pictureRepo.add(picture)
        }
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
