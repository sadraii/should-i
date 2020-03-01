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

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.repository.PictureRepository
import com.sadraii.shouldi.data.repository.UserRepository
import com.sadraii.shouldi.toByteArrayWebp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class PictureFirebaseDataStore {

    companion object {
        const val PICTURE_FORMAT = "webp"
    }

    private val storageRef = FirebaseStorage.getInstance(ShouldIDatabase.GS_BUCKET).reference

    internal suspend fun add(pictureEntity: PictureEntity, picture: Bitmap) {
        // Await() is called on Tasks to ensure picture is uploaded before RecyclerView loads list of pictures.
        withContext(Dispatchers.IO) {
            val storage = async {
                storageRef.child(pictureEntity.pictureUrl)
                    .putBytes(
                        picture.toByteArrayWebp(),
                        storageMetadata { contentType = "image/$PICTURE_FORMAT" })
                    .addOnFailureListener { e ->
                        Log.d(TAG, "Failed to add picture ${pictureEntity.id} to Storage: ", e)
                    }.await()
            }

            val userRef = Firebase.firestore.collection(UserRepository.USERS_PATH)
                .document(pictureEntity.userId)

            val user = async {
                userRef.collection(PictureRepository.PICTURES_PATH)
                    .document(pictureEntity.id)
                    .set(pictureEntity)
                    .addOnFailureListener { e ->
                        Log.d(TAG, "Failed to add picture ${pictureEntity.id} to Firestore", e)
                    }.await()
            }

            // TODO these can throw exception
            storage.await()
            user.await()
        }
    }

    internal suspend fun updatePictureVoteCount(picture: PictureEntity, vote: Boolean) {
        val picSnapshot = try {
            Firebase.firestore.collectionGroup(PictureRepository.PICTURES_PATH)
                .whereEqualTo("userId", picture.userId)
                .whereEqualTo("created", picture.created)
                .limit(1)
                .get().await()
        } catch (e: FirebaseFirestoreException) {
            Log.d(TAG, e.localizedMessage!!)
            Log.d(TAG, "Failed to get picture snapshot ${picture.id} from Firestore")
            null
        }
        if (picSnapshot != null && !picSnapshot.isEmpty) {
            val picRef = picSnapshot.documents[0].reference
            val newVotes = getCurrentVotes(picRef, vote) + 1
            picRef.update(if (vote) mapOf("yesVotes" to newVotes) else mapOf("noVotes" to newVotes))
                .addOnFailureListener { e ->
                    Log.d(TAG, "Failed to update picture vote count ${picture.id} in Firestore", e)
                }
        }
    }

    private suspend fun getCurrentVotes(picRef: DocumentReference, vote: Boolean): Int {
        val picObject = try {
            picRef.get().await()
        } catch (e: FirebaseFirestoreException) {
            Log.d(TAG, e.localizedMessage!!)
            Log.d(TAG, "Failed to get picture ref ${picRef.id} from Firestore")
            null
        }
        return if (vote) {
            picObject?.toObject(PictureEntity::class.java)!!.yesVotes
        } else {
            picObject?.toObject(PictureEntity::class.java)!!.noVotes
        }
    }
}



