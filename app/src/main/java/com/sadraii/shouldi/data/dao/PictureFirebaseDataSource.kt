package com.sadraii.shouldi.data.dao

import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storageMetadata
import com.sadraii.shouldi.TAG
import com.sadraii.shouldi.data.ShouldIDatabase
import com.sadraii.shouldi.data.entity.PictureEntity
import com.sadraii.shouldi.data.repository.PictureRepository
import com.sadraii.shouldi.data.repository.UserRepository
import com.sadraii.shouldi.toByteArray

class PictureFirebaseDataSource {

    companion object {

        private const val PICTURE_FORMAT = "webp" // Will also need to change Bitmap.toByteArray() extension
    }

    private val user = FirebaseAuth.getInstance().currentUser
    private val userRef = Firebase.firestore.collection(UserRepository.USERS_PATH).document(user!!.uid)
    private val storageRef = FirebaseStorage.getInstance(ShouldIDatabase.GS_BUCKET).reference

    internal fun add(pictureEntity: PictureEntity, picture: Bitmap) {
        val picturePath = "${pictureEntity.userId}/${pictureEntity.id}.$PICTURE_FORMAT"
        val pictureRef = storageRef.child(picturePath)
        pictureRef.putBytes(
            picture.toByteArray(),
            storageMetadata { contentType = "image/$PICTURE_FORMAT" }) // TODO Can metadata be skipped?
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to add picture ${pictureEntity.id} to Storage: ", e)
            }

        userRef.collection(PictureRepository.PICTURES_PATH)
            .document(pictureEntity.id)
            .set(pictureEntity)
            .addOnFailureListener { e ->
                Log.d(TAG, "Failed to add picture ${pictureEntity.id} to Firestore", e)
            }
    }
}