package com.github.michalhodan.jiraalert.storage

import java.net.URL
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UrlImage(private val context: Activity, private val filename: String) {

    fun image(url: String, onLoaded: (Bitmap) -> Unit) = GlobalScope.launch(Dispatchers.IO) {
        val image = context.getFileStreamPath(filename)?.takeIf {
            it.exists()
        }?.let {
            context.openFileInput(filename).use { stream -> BitmapFactory.decodeStream(stream) }
        } ?: URL(url).openStream().use {
            BitmapFactory.decodeStream(it)
        }.apply {
            context.openFileOutput(filename, Context.MODE_PRIVATE).use {
                compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }

       image.scale().let {
           context.runOnUiThread { onLoaded(it) }
        }
    }

    private fun Bitmap.scale() = Bitmap.createScaledBitmap(this, 128, 128, true)
}