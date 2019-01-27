package com.github.tatsuhama.firebase_hands_on

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage

class ImageItemViewHolder(view: View, private val action: Action) : RecyclerView.ViewHolder(view) {

    fun bind(imageItem: ImageItem) {
        val imageView = itemView.findViewById<ImageView>(R.id.image)
        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child("images/${imageItem.fileName}").downloadUrl.addOnSuccessListener {
            Glide.with(imageView.context).load(it).into(imageView)
        }.addOnFailureListener {
            // Handle any errors
        }

        itemView.findViewById<TextView>(R.id.fileNameText).text = imageItem.fileName
        itemView.setOnClickListener { action.openDetailImage(imageItem) }
    }

    interface Action {
        fun openDetailImage(imageItem: ImageItem)
    }

}