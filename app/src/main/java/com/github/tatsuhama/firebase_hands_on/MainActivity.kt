package com.github.tatsuhama.firebase_hands_on

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val outputUri: Uri by lazy {
        FileProvider.getUriForFile(
            this@MainActivity,
            BuildConfig.APPLICATION_ID + ".provider",
            createOutputFile()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, outputUri)
                .addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, REQUEST_CODE_CAMERA)
        }

        image_list.adapter = ImageItemAdapter()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logout -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        startActivity(Intent(this@MainActivity, LaunchActivity::class.java))
                        finish()
                    }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, outputUri)
            upload(bitmap)
        }
    }

    private fun upload(bitmap: Bitmap) {
        val fileName = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Date()) + ".jpg"
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val imagesFolderRef = FirebaseStorage.getInstance().reference.child("images")
        val uploadTask = imagesFolderRef.child(fileName).putBytes(baos.toByteArray())
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val collectionRef = FirebaseFirestore.getInstance().collection("images")
                collectionRef.add(mapOf("fileName" to fileName))
            }
        }
    }

    private fun createOutputFile(): File =
        File(filesDir, "temp/photo.jpg").apply {
            if (!exists()) {
                parentFile.mkdirs()
                createNewFile()
            }
        }

    companion object {
        private const val REQUEST_CODE_CAMERA = 123
    }
}
