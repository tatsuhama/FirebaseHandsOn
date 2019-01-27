package com.github.tatsuhama.firebase_hands_on

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val storageRef = FirebaseStorage.getInstance().reference
        storageRef.child("images/sudachikun.jpg").downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(image)
        }.addOnFailureListener {
            // Handle any errors
        }
    }
}
