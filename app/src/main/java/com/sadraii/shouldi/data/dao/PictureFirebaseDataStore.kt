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
import kotlinx.coroutines.tasks.await

class PictureFirebaseDataStore {

    companion object {

        const val PICTURE_FORMAT = "webp"
    }

    // private val user = FirebaseAuth.getInstance().currentUser
    private val storageRef = FirebaseStorage.getInstance(ShouldIDatabase.GS_BUCKET).reference

    internal fun add(pictureEntity: PictureEntity, picture: Bitmap) {
        storageRef.child(pictureEntity.pictureUrl)
            .putBytes(
                picture.toByteArrayWebp(),
                storageMetadata { contentType = "image/$PICTURE_FORMAT" }) // TODO Can metadata be skipped?
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to add picture ${pictureEntity.id} to Storage: ", e)
            }

        val userRef = Firebase.firestore.collection(UserRepository.USERS_PATH)
            .document(pictureEntity.userId)
        // TODO Remove
        userRef.collection(PictureRepository.PICTURES_PATH).get().addOnSuccessListener {
            Log.d(TAG, "size=before add: ${it.size()}")
        }

        userRef.collection(PictureRepository.PICTURES_PATH)
            .document(pictureEntity.id)
            .set(pictureEntity)
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to add picture ${pictureEntity.id} to Firestore", e)
            }

        // TODO remove
        userRef.collection(PictureRepository.PICTURES_PATH).get().addOnSuccessListener {
            Log.d(TAG, "size=after add: ${it.size()}")
        }
    }

    internal suspend fun updatePictureVoteCount(picture: PictureEntity, vote: Boolean) {
        val picSnapshot = Firebase.firestore.collectionGroup(PictureRepository.PICTURES_PATH)
            .whereEqualTo("userId", picture.userId)
            .whereEqualTo("created", picture.created)
            .limit(1)
            .get().await()
        if (!picSnapshot.isEmpty) {
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

