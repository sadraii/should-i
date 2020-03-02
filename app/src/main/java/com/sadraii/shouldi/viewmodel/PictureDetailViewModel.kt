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
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.PictureFirebaseDataStore
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.repository.PictureRepository
import kotlinx.coroutines.launch

class PictureDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val pictureDao: PictureDao
    private val pictureRepo: PictureRepository
    private var _pictureDeleted = MutableLiveData<Boolean>()
    internal var pictureDeleted: LiveData<Boolean> = _pictureDeleted

    init {
        val db = ShouldIDatabase.getDatabase(application, viewModelScope)
        pictureDao = db.pictureDao()
        pictureRepo = PictureRepository(pictureDao, PictureFirebaseDataStore())
    }

    internal fun delete(pictureEntity: PictureEntity) {
        viewModelScope.launch {
            _pictureDeleted.value = false
            try {
                pictureRepo.delete(pictureEntity)
            } catch (e: Throwable) {
                Log.d(TAG, "Failed to delete picture", e)
            } finally {
                _pictureDeleted.value = true
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PictureDetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PictureDetailViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct PictureDetailViewModel")
        }
    }
}



