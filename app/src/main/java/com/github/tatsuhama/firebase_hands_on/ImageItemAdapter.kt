package com.github.tatsuhama.firebase_hands_on

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.google.firebase.firestore.FirebaseFirestore

class ImageItemAdapter(private val action: ImageItemViewHolder.Action) : RecyclerView.Adapter<ImageItemViewHolder>() {

    private var items = listOf<ImageItem>()

    init {
        val collectionReference = FirebaseFirestore.getInstance().collection("images")
        collectionReference.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            notifyDataSetChanged()
            items = querySnapshot?.documents?.map { ImageItem.from(it) } ?: listOf()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
        return ImageItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false), action)
    }

    override fun getItemCount(): Int = items.count()

    override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

}