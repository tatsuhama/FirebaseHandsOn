package com.github.tatsuhama.firebase_hands_on

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage

class ImageItemViewHolder(view: View, private val action: Action) : RecyclerView.ViewHolder(view) {

    fun bind(imageItem: ImageItem) {
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child("images/${imageItem.thumbnailFileName}").downloadUrl.addOnSuccessListener {
            Glide.with(imageView.context).load(it).into(imageView)
        }.addOnFailureListener {
            // Handle any errors
        }

        itemView.findViewById<TextView>(R.id.fileNameText).apply {
            text = imageItem.fileName
            setTextColor(getTextColor())
        }
        itemView.setOnClickListener { action.openDetailImage(imageItem) }
    }

    private fun getTextColor(): Int {
        val remoteConfig = FirebaseRemoteConfig.getInstance().apply {
            setDefaults(mapOf("text_color" to "#FF0000"))
        }
        val color = remoteConfig.getString("text_color")
        return Color.parseColor(color)
    }

    interface Action {
        fun openDetailImage(imageItem: ImageItem)
    }

}