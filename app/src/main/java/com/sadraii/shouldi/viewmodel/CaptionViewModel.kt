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
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.PictureFirebaseDataStore
import com.sadraii.shouldi.data.repository.PictureRepository
import kotlinx.coroutines.launch

class CaptionViewModel(application: Application) : AndroidViewModel(application) {

    private val pictureDao: PictureDao
    private val pictureRepo: PictureRepository
    private var _pictureAdded = MutableLiveData<Boolean>()
    internal val pictureAdded: LiveData<Boolean> = _pictureAdded

    init {
        val db = ShouldIDatabase.getDatabase(application, viewModelScope)
        pictureDao = db.pictureDao()
        pictureRepo = PictureRepository(pictureDao, PictureFirebaseDataStore())
    }

    // TODO look into StorageException
    internal fun addPicture(picture: Bitmap, caption: String) {
        viewModelScope.launch {
            _pictureAdded.value = false
            try {
                pictureRepo.add(picture, caption)
            } finally {
                _pictureAdded.value = true
            }
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



