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

import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.dao.UserFirebaseDao
import com.sadraii.shouldi.data.entity.UserEntity

class UserRepository(private val userDao: UserDao, private val userFirebaseDao: UserFirebaseDao) {

    companion object {

        internal const val USERS_PATH = "users"
    }

    /** If user exists, update it. If not, add it. */
    internal suspend fun addOrUpdate(user: UserEntity) {
        val dbUser = userDao.getUser(user.id)
        if (dbUser?.id == user.id) {
            userDao.update(user)
        } else {
            userDao.insert(user)
        }

        userFirebaseDao.addOrUpdate(user)
    }
}

