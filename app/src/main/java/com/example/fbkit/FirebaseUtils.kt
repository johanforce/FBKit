package com.example.fbkit

import android.graphics.Bitmap
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer

class FirebaseUtils {
    fun recognizeText(bitmap: Bitmap?, result: ((data: String) -> Unit)? = null) {
        if (bitmap == null) return
        val image = FirebaseVisionImage.fromBitmap(bitmap)

        //Nếu dùng phải trả phí
        // val options = FirebaseVisionCloudTextRecognizerOptions.Builder()
        //     .setLanguageHints(listOf("zh"))
        //     .build()
        //
        // val textRecognizer: FirebaseVisionTextRecognizer =
        //     FirebaseVision.getInstance().getCloudTextRecognizer(options)

        val textRecognizer: FirebaseVisionTextRecognizer =
            FirebaseVision.getInstance().onDeviceTextRecognizer

        textRecognizer.processImage(image)
            .addOnSuccessListener { data ->
                result?.invoke(data.text)
            }
            .addOnFailureListener { e ->
                result?.invoke(e.toString())
            }
    }
}