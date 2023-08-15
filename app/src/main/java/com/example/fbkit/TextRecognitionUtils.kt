package com.example.fbkit

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.chinese.ChineseTextRecognizerOptions
import com.google.mlkit.vision.text.japanese.JapaneseTextRecognizerOptions
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextRecognitionUtils {
    fun getTextFromDrawable(context: Context, drawableResId: Uri, result: ((data: String) -> Unit)? = null) {
        try {
            val bitmap: Bitmap = Utils.uriToBitmap(drawableResId, context) ?: return

            val inputImage = InputImage.fromBitmap(bitmap, 0)

            // val textRecognizer = TextRecognition.getClient(JapaneseTextRecognizerOptions.Builder().build())
            val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            textRecognizer.process(inputImage)
                .addOnSuccessListener { visionText ->
                    result?.invoke(visionText.text)
                }
                .addOnFailureListener { e ->
                    result?.invoke(e.toString())
                }
        } catch (e: Exception) {
            result?.invoke(e.toString())
        }
    }
}