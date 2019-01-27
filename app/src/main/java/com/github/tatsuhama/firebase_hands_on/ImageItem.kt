package com.github.tatsuhama.firebase_hands_on

import com.google.firebase.firestore.DocumentSnapshot

data class ImageItem(val id: String, val fileName: String) {

    companion object {
        fun from(documentSnapShot: DocumentSnapshot): ImageItem {
            val data = documentSnapShot.data ?: throw IllegalArgumentException("data is null")
            val fileName: String = data["fileName"] as String
            return ImageItem(documentSnapShot.id, fileName)
        }
    }

}