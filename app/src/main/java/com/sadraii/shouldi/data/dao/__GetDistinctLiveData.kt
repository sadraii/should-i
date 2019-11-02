/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sadraii.shouldi.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.sadraii.shouldi.data.entity.UserEntity


/**
 * Only use this on small tables that need live updates
 */
@Dao
abstract class DistinctDao {

    /**
     * Get a user by userId.
     * @return the user from the table with a specific userId.
     */
    @Query("SELECT * FROM users WHERE user_id = :userId")
    protected abstract fun getUserById(userId: Int): LiveData<UserEntity>

    fun getUserByIdDistinctLiveData(userId: Int): LiveData<UserEntity> = getUserById(userId).getDistinct()
}

