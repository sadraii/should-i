package com.sadraii.shouldi.data.dao

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.entity.UserEntity
import com.sadraii.shouldi.data.repository.UserRepository

class UserFirebaseDataSource {

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
                        "lastOnline" to lastOnline
                    )
                }
            )
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to update user ${user.id} in Firestore", e)
            }
    }
}