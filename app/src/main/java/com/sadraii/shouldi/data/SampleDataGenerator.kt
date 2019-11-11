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

package com.sadraii.shouldi.data

import android.util.Log
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.entity.UserEntity
import java.util.Date

object SampleDataGenerator {

    suspend fun generate(userDao: UserDao, pictureDao: PictureDao) {
        userDao.deleteAllUsers()

        var newUser = UserEntity(1, "user1", "Elise", "Elser", "elise@example.com", Date(), null)
        userDao.insert(newUser)

        newUser = UserEntity(2, "user2", "Kendra", "Kahn", "kendra@example.com", Date(), Date())
        userDao.insert(newUser)

        newUser = UserEntity(3, "user3", "Corey", "Coursey", "corey@example.com", Date(), null)
        userDao.insert(newUser)

        newUser = UserEntity(4, "user4", "Kyong", "Kreger", "kyong@example.com", Date(), Date())
        userDao.insert(newUser)

        newUser = UserEntity(5, "user5", "Mia", "Minnick", "mia@example.com", Date(), Date())
        userDao.insert(newUser)

        newUser = UserEntity(6, "user6", "Robby", "Reedy", "robby@example.com", Date(), Date())
        userDao.insert(newUser)

        newUser = UserEntity(7, "user7", "Peggy", "Potter", "peggy@example.com", Date(), Date())
        userDao.insert(newUser)

        pictureDao.deleteAllPictures()

        var newPicture = PictureEntity(
            id = 11,
            pictureUrl = "https://www.example.com/pic1",
            created = Date(),
            caption = "Should I picture 1",
            userId = 1
        )
        pictureDao.insert(newPicture)

        newPicture = PictureEntity(
            22,
            "https://www.example.com/pic2",
            Date(),
            4,
            2,
            1,
            Date(),
            Date(),
            1,
            "Should I picture 2",
            2
        )
        pictureDao.insert(newPicture)

        newPicture = PictureEntity(
            id = 33,
            pictureUrl = "https://www.example.com/pic3",
            created = Date(),
            caption = "Should I picture 3",
            userId = 3
        )
        pictureDao.insert(newPicture)

        newPicture = PictureEntity(
            111,
            "https://www.example.com/pic111",
            Date(),
            100,
            90,
            50,
            Date(),
            Date(),
            3,
            "Should I picture 1 again",
            1
        )
        pictureDao.insert(newPicture)

        newPicture = PictureEntity(
            id = 66,
            pictureUrl = "https://www.example.com/pic6",
            created = Date(),
            caption = "Should I picture 6",
            userId = 1
        )
        pictureDao.insert(newPicture)

        newPicture = PictureEntity(
            id = 77,
            pictureUrl = "https://www.example.com/pic7",
            created = Date(),
            caption = "Should I picture 7",
            userId = 1
        )
        pictureDao.insert(newPicture)

        newPicture = PictureEntity(
            id = 88,
            pictureUrl = "https://www.example.com/pic8",
            created = Date(),
            caption = "Should I picture 8",
            userId = 1
        )
        pictureDao.insert(newPicture)
        Log.d(TAG, "populated database")
    }
}