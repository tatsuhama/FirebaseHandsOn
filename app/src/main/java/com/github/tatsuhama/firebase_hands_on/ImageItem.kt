package com.github.tatsuhama.firebase_hands_on

import android.os.Parcelable
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ImageItem(val id: String, val fileName: String) : Parcelable {

    companion object {
        fun from(documentSnapShot: DocumentSnapshot): ImageItem {
            val data = documentSnapShot.data ?: throw IllegalArgumentException("data is null")
            val fileName: String = data["fileName"] as String
            return ImageItem(documentSnapShot.id, fileName)
        }
    }

}