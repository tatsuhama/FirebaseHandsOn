package com.github.tatsuhama.firebase_hands_on

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val imageItem = intent.getParcelableExtra<ImageItem>(KEY_IMAGE_ITEM)
        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child("images/${imageItem.fileName}").downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(image)
        }.addOnFailureListener {
            // Handle any errors
        }
    }

    companion object {

        private const val KEY_IMAGE_ITEM = "KEY_IMAGE_ITEM"

        fun newIntent(context: Context, imageItem: ImageItem): Intent =
            Intent(context, DetailActivity::class.java)
                .putExtra(KEY_IMAGE_ITEM, imageItem)
    }
}
