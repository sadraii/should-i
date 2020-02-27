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

package com.sadraii.shouldi.data.dao

import android.util.Log
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.entity.UserEntity
import com.sadraii.shouldi.data.repository.PictureRepository
import com.sadraii.shouldi.data.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserFirebaseDataStore {

    private val userCollection = Firebase.firestore.collection(UserRepository.USERS_PATH)

    internal fun addOrUpdate(user: UserEntity) {
        userCollection.whereEqualTo("id", user.id)
            .limit(1).get()
            .addOnCompleteListener { task ->
                with(task) {
                    if (isSuccessful) {
                        if (result!!.isEmpty) {
                            addUser(user)
                        } else {
                            updateUser(user)
                        }
                    } else {
                        Log.d(TAG, "Error getting user ${user.id} from Firestore: ", exception)
                    }
                }
            }
    }

    private fun addUser(user: UserEntity) {
        userCollection.document(user.id)
            .set(user)
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to create user ${user.id} in Firestore", e)
            }
    }

    private fun updateUser(user: UserEntity) {
        userCollection.document(user.id)
            .update(
                with(user) {
                    mapOf(
                        "userName" to userName,
                        "email" to email,
                        "lastOnline" to lastOnline,
                        "lastVote" to lastVote,
                        "photoUrl" to photoUrl
                    )
                }
            )
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to update user ${user.id} in Firestore", e)
            }
    }

    internal suspend fun updateLastVote(user: FirebaseUser, lastVote: Long) {
        userCollection.document(user.uid)
            .update(mapOf("lastVote" to lastVote))
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to update vote for ${user.uid} in Firestore", e)
            }
    }

    // TODO finish
    internal suspend fun getUser(user: String?) =
        user?.let {
            withContext(Dispatchers.IO) {
                val userDoc = try {
                    userCollection.document(user).get().await()
                } catch (e: FirebaseFirestoreException) {
                    Log.d(TAG, e.localizedMessage!!)
                    Log.d(TAG, "Failed to get user ref ${user} from Firestore")
                    null
                }
                userDoc?.toObject(UserEntity::class.java)
            }
        }

    internal suspend fun nextPictureOrNull(firebaseUser: FirebaseUser) =
        withContext(Dispatchers.IO) {
            val userDoc = try {
                userCollection.document(firebaseUser.uid).get().await()
            } catch (e: FirebaseFirestoreException) {
                Log.d(TAG, e.localizedMessage!!)
                Log.d(TAG, "Failed to get user ref ${firebaseUser.uid} from Firestore")
                null
            }
            val userEntity = userDoc?.toObject(UserEntity::class.java)
            getNextPicture(userEntity?.lastVote)
        }

    // TODO Can't vote for your own picture
    private suspend fun getNextPicture(lastVote: Long?): PictureEntity? {
        val nextPicSnapshot = try {
            if (lastVote == null) {
                Firebase.firestore.collectionGroup(PictureRepository.PICTURES_PATH)
                    .orderBy("created")
                    .limit(1)
                    .get().await()
            } else {
                Firebase.firestore.collectionGroup(PictureRepository.PICTURES_PATH)
                    .whereGreaterThan("created", lastVote)
                    .orderBy("created")
                    .limit(1)
                    .get().await()
            }
        } catch (e: FirebaseFirestoreException) {
            Log.d(TAG, e.message!!)
            null
        }
        Log.d(TAG, "collectionGroup size=${nextPicSnapshot?.size()}")

        return if (nextPicSnapshot != null && !nextPicSnapshot.isEmpty) {
            nextPicSnapshot.documents[0].toObject(PictureEntity::class.java)
        } else {
            null
        }
    }
}



