package com.example.fbkit

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class MainActivity : ComponentActivity() {
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private const val REQUEST_IMAGE_CAPTURE = 429
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }

    val tvText = lazy {
        findViewById<TextView>(R.id.tvText)
    }
    val ivImage = lazy {
        findViewById<ImageView>(R.id.ivImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val captureButton = findViewById<Button>(R.id.captureButton)
        captureButton.setOnClickListener {
            handleCamera()
        }
    }

    private fun handleCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(this, CamActivity::class.java)
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(this, CamActivity::class.java)
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
            } else {

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val savedUri = data?.data
            if (savedUri != null) {
                Glide.with(this)
                    .load(savedUri)
                    .into(ivImage.value)

                // TextRecognitionUtils().getTextFromDrawable(this, savedUri) {
                //     tvText.value.text = it
                // }
                FirebaseUtils().recognizeText(Utils.uriToBitmap(savedUri, context = this)){
                    tvText.value.text = it
                }
            }
        }
    }
}
