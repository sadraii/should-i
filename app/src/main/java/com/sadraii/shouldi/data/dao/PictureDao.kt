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

package com.sadraii.shouldi.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.sadraii.shouldi.data.entity.PictureEntity

@Dao
interface PictureDao : BaseDao<PictureEntity> {

    @Query("SELECT * FROM pictures WHERE picture_id = :pictureId")
    suspend fun getPicture(pictureId: Int): PictureEntity

    @Query("DELETE FROM pictures")
    suspend fun deleteAllPictures()

    @Query("SELECT * FROM pictures WHERE owner_id = :userId")
    suspend fun getPicturesForUser(userId: Int): List<PictureEntity>
}

