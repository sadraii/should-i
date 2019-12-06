/*
 * Copyright 2019 Mostafa Sadraii
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

package com.sadraii.shouldi.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.PictureFirebaseDataSource
import com.sadraii.shouldi.data.entity.PictureEntity
import java.util.UUID

class PictureRepository(
    private val pictureDao: PictureDao,
    private val pictureFirebaseDataSource: PictureFirebaseDataSource
) {

    companion object {

        internal const val PICTURES_PATH = "pictures"
    }

    private val user = FirebaseAuth.getInstance().currentUser

    internal suspend fun add(picture: Bitmap) {
        // TODO add actual bitmap?
        val uuid = UUID.randomUUID().toString()
        val uri = Uri.Builder()
            .authority(ShouldIDatabase.GS_BUCKET)
            .appendPath(user!!.uid)
            .appendPath("$uuid.${PictureFirebaseDataSource.PICTURE_FORMAT}")
            .build().toString()
        val pictureToAdd = PictureEntity(uuid, user.uid, uri)

        val dbPicture = pictureDao.getPicture(uuid)
        if (dbPicture?.id != uuid) {
            pictureDao.insert(pictureToAdd)
        }

        pictureFirebaseDataSource.add(pictureToAdd, picture)
    }
}