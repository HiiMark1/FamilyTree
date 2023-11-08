package com.example.myapplication.android.data

import android.net.Uri
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class FirebaseStorageServiceImpl(
    val firebaseStoragePhotosRef: StorageReference,
    val firebaseStorageVideosRef: StorageReference,
) {
    suspend fun uploadPhotoAndGetIsCompleted(uri: Uri, uid: String, i: Int): Boolean {
        var isCompleted = false

        firebaseStoragePhotosRef.child(uid).child(i.toString()).putFile(uri)
            .addOnSuccessListener {
                isCompleted = true
            }
            .addOnFailureListener {
                isCompleted = false
            }.await()

        return isCompleted
    }

    suspend fun getPhotoUri(uid: String, i: Int): Uri? {
        var downloadedUri: Uri? = null
        firebaseStoragePhotosRef.child(uid).child(i.toString()).downloadUrl
            .addOnSuccessListener {
                downloadedUri = it
            }
            .addOnFailureListener{
                return@addOnFailureListener
            }.await()

        return downloadedUri
    }
}