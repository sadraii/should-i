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
import com.sadraii.shouldi.data.entity.PictureEntity

class PicturesViewModel(application: Application) : AndroidViewModel(application) {

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
            if (modelClass.isAssignableFrom(PicturesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return PicturesViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct PicturesViewModel")
        }
    }
}

