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
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "email" to email,
                        "lastOnline" to lastOnline,
                        "lastVote" to lastVote
                    )
                }
            )
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to update user ${user.id} in Firestore", e)
            }
    }

    internal fun updateVote(user: UserEntity) {
        userCollection.document(user.id)
            .update(
                with(user) {
                    mapOf("lastVote" to lastVote)
                }
            )
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to update vote for ${user.id} in Firestore", e)
            }
    }

    // TODO fix
    internal suspend fun getUser(firebaseUser: FirebaseUser) =
        withContext(Dispatchers.IO) {
            val userDoc = try {
                userCollection.document(firebaseUser.uid).get().await()
            } catch (e: FirebaseFirestoreException) {
                Log.d(TAG, e.localizedMessage!!)
                null
            }
            userDoc?.toObject(UserEntity::class.java)
        }

    internal suspend fun nextPictureOrNull(firebaseUser: FirebaseUser) =
        withContext(Dispatchers.IO) {
            val userDoc = try {
                userCollection.document(firebaseUser.uid).get().await()
            } catch (e: FirebaseFirestoreException) {
                Log.d(TAG, e.localizedMessage!!)
                null
            }
            val userEntity = userDoc?.toObject(UserEntity::class.java)
            getPicture(userEntity?.lastVote)
        }

    private suspend fun getPicture(lastVote: Long?): PictureEntity? {
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
        Log.d(TAG, "collectionGroup size=${nextPicSnapshot?.documents?.size}")

        return if (nextPicSnapshot != null && !nextPicSnapshot.isEmpty) {
            nextPicSnapshot.documents[0].toObject(PictureEntity::class.java)
        } else {
            null
        }
    }
}

