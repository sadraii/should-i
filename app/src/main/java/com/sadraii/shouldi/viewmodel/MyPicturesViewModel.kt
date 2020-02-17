package com.sadraii.shouldi.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sadraii.shouldi.data.entity.PictureEntity

class MyPicturesViewModel(application: Application) : AndroidViewModel(application) {

    // private val pictureDao: PictureDao
    // private val pictureRepo: PictureRepository
    // private val currentUser = FirebaseAuth.getInstance().currentUser?.uid
    // private val storage = Firebase.storage(ShouldIDatabase.GS_BUCKET)
    private var _pictures =
        MutableLiveData<List<PictureEntity>>().apply { value = emptyList() }
    internal val pictures: LiveData<List<PictureEntity>> = _pictures

    init {
        // val db = ShouldIDatabase.getDatabase(application, viewModelScope)
        // pictureDao = db.pictureDao()
        // pictureRepo = PictureRepository(pictureDao, PictureFirebaseDataStore())
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MyPicturesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MyPicturesViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct MyPicturesViewModel")
        }
    }
}