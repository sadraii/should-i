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

package com.sadraii.shouldi.data

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.entity.UserEntity
import com.sadraii.shouldi.data.repository.PictureRepository
import com.sadraii.shouldi.data.repository.UserRepository
import java.time.Instant
import java.util.UUID
import kotlin.random.Random

private const val numberOfSamples = 15

object SampleData {

    suspend fun delete(userDao: UserDao, pictureDao: PictureDao) {
        userDao.deleteAllUsers()
        pictureDao.deleteAllPictures()
        Log.d(TAG, "Deleted database data")
    }

    suspend fun generateUserAndPicture(userDao: UserDao, pictureDao: PictureDao) {
        val userUuid = UUID.randomUUID().toString()
        val user = with(SampleArrays) {
            UserEntity(
                userUuid,
                usernameOrNull(0),
                emailOrNull(0),
                randomPastTime(),
                timeOrNull()
            )
        }
        val picture = PictureEntity(
            UUID.randomUUID().toString(),
            userUuid,
            randomPictureUrl(),
            caption(0),
            randomShortPastTime(),
            randomCount(),
            randomCount()
        )
        userDao.insert(user)
        pictureDao.insert(picture)

        Log.d(TAG, "Generated database user and picture")
    }

    suspend fun generate(userDao: UserDao, pictureDao: PictureDao) {
        val firestore = Firebase.firestore
        val usersCollection = firestore.collection(UserRepository.USERS_PATH)

        for (i in 0 until numberOfSamples) {
            val userUuid = UUID.randomUUID().toString()
            val user = with(SampleArrays) {
                UserEntity(
                    userUuid,
                    usernameOrNull(i),
                    emailOrNull(i),
                    randomPastTime(),
                    timeOrNull()
                )
            }
            val picture = PictureEntity(
                UUID.randomUUID().toString(),
                userUuid,
                randomPictureUrl(),
                caption(i),
                randomShortPastTime(),
                randomCount(),
                randomCount()
            )

            userDao.insert(user)
            pictureDao.insert(picture)

            val userRef = usersCollection.document(user.id)
            userRef.set(user)
                .addOnSuccessListener {
                    userRef.collection(PictureRepository.PICTURES_PATH)
                        .document(picture.id)
                        .set(picture)
                        .addOnFailureListener { e ->
                            Log.d(TAG, "Failed to create picture ${picture.id}", e)
                        }
                }
                .addOnFailureListener { e ->
                    Log.d(TAG, "Failed to create user ${user.id}", e)
                }
        }
        Log.d(TAG, "Generated database data")
    }

    private fun usernameOrNull(i: Int) = when (Random.nextInt(2)) {
        0 -> SampleArrays.userName[i]
        else -> null
    }

    private fun caption(i: Int) = SampleArrays.caption[i]

    private fun emailOrNull(i: Int) = when (Random.nextInt(2)) {
        0 -> with(SampleArrays) {
            "${firstName[i]}.${lastName[i]}@example.com"
        }
        else -> null
    }

    private fun timeOrNull() = when (Random.nextInt(2)) {
        0 -> Instant.now().minusSeconds(Random.nextLong(100, 200)).toEpochMilli()
        else -> null
    }

    private fun randomPictureUrl(): String {
        val randX = Random.nextInt(500, 800)
        val randY = Random.nextInt(500, 800)
        return "http://placekitten.com/$randX/$randY"
    }

    private fun randomPastTime() = Instant.now().minusSeconds(Random.nextLong(10_000, 20_000)).toEpochMilli()

    private fun randomShortPastTime() = Instant.now().minusSeconds(Random.nextLong(1_000, 2_000)).toEpochMilli()

    private fun randomCount() = Random.nextInt(300)

    object SampleArrays {
        val userName = arrayOf(
            "creasehygiea",
            "babymeg",
            "richeskerchief",
            "nantllecollect",
            "kirkwoodbrunton",
            "dapperitil",
            "triptakerradon",
            "gleefulecotone",
            "appendchamois",
            "quitjargon",
            "bowiedriller",
            "reversingraid",
            "yieldkilt",
            "smolderdrove",
            "jossmuschi"
        )
        val firstName = arrayOf(
            "Bjoern",
            "Jehovah",
            "Mariele",
            "Gidon",
            "Ukaleq",
            "Cristiano",
            "Evangeliya",
            "Wilhelmina",
            "Agrafena",
            "Gracjan",
            "Severin",
            "Parvin",
            "Agung",
            "Catharina",
            "Jarah"
        )
        val lastName = arrayOf(
            "Coble",
            "Borack",
            "Lowey",
            "Lestition",
            "Wheeler",
            "Presky",
            "Perrimon",
            "Chow",
            "Bradshaw",
            "Leandro",
            "Chinman",
            "Banta",
            "Redfern",
            "Serrell",
            "Cancelliere"
        )
        val caption = arrayOf(
            "bubble ",
            "wiggly",
            "spooky",
            "table",
            "survive",
            "holiday",
            "forbid",
            "rest",
            "precious",
            "support",
            "toss",
            "pass",
            "blossom",
            "install",
            "addition",
            "mellow"
        )
    }
}


