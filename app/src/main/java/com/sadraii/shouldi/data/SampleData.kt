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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.dao.PictureDao
import com.sadraii.shouldi.data.dao.UserDao
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.entity.UserEntity
import java.time.Instant
import java.util.UUID
import kotlin.random.Random

object SampleData {

    suspend fun delete(userDao: UserDao, pictureDao: PictureDao) {
        userDao.deleteAllUsers()
        pictureDao.deleteAllPictures()
        Log.d(TAG, "Deleted database data")
    }

    suspend fun generate(userDao: UserDao, pictureDao: PictureDao) {
        val firestore = Firebase.firestore
        val usersCollection = firestore.collection("users")

        for (i in 0..14) {
            val userUuid = UUID.randomUUID().toString()
            val user = UserEntity(
                userUuid,
                SampleArrays.userName[i], /* TODO(randomly return null?) */
                SampleArrays.firstName[i], /* TODO(randomly return null?) */
                SampleArrays.lastName[i], /* TODO(randomly return null?) */
                emailOrNull(i),
                randomPastTime(),
                timeOrNull(i)
            )
            val picture = PictureEntity(
                UUID.randomUUID().toString(),
                userUuid,
                randomPictureUrl(),
                randomShortPastTime(),
                randomCount(),
                randomCount(),
                randomCount(),
                timeOrNull(i),
                timeOrNull(i),
                Random.nextInt(3),
                ""
            )

            userDao.insert(user)
            pictureDao.insert(picture)

            val userRef = usersCollection.document(user.id)
            userRef.set(user)
                .addOnSuccessListener {
                    userRef.collection("pictures")
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

    private fun emailOrNull(i: Int) = when (Random.nextInt(2)) {
        0 -> "${SampleArrays.firstName[i]}.${SampleArrays.lastName[i]}@example.com"
        else -> null
    }

    private fun timeOrNull(i: Int) = when (Random.nextInt(2)) {
        0 -> Instant.now().minusSeconds(Random.nextLong(100, 200))
        else -> null
    }

    private fun randomPictureUrl(): String {
        val randX = Random.nextInt(500, 800)
        val randY = Random.nextInt(500, 800)
        return "http://placekitten.com/$randX/$randY"
    }

    private fun randomPastTime() = Instant.now().minusSeconds(Random.nextLong(10_000, 20_000))

    private fun randomShortPastTime() = Instant.now().minusSeconds(Random.nextLong(1_000, 2_000))

    private fun randomCount() = Random.nextInt(300)
}

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
}

