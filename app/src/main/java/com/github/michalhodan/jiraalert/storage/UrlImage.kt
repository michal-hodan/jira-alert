package com.github.michalhodan.jiraalert.storage

import java.net.URL
import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.math.BigInteger
import java.security.MessageDigest
import java.io.*

class UrlImage(private val context: Activity, private val path: String) {

    var scale = 48

    fun image(url: String, onLoaded: (Bitmap) -> Unit) = GlobalScope.launch(Dispatchers.IO) {
        val file = File(context.cacheDir, "$path-${url.md5()}")

        val image = file.takeIf {
            it.exists()
        }?.let {
            FileInputStream(file).use { stream -> BitmapFactory.decodeStream(stream) }
        } ?: URL(url).openStream().use {
            BitmapFactory.decodeStream(it)
        }?.apply {
            FileOutputStream(file).use {
                compress(Bitmap.CompressFormat.PNG, 100, it)
            }
        }

       image?.scale()?.let {
           context.runOnUiThread { onLoaded(it) }
        }
    }

    private fun Bitmap.scale() = Bitmap.createScaledBitmap(this, scale, scale, true)

    companion object {
        fun user(activity: Activity) = UrlImage(activity, "user")
        fun project(activity: Activity) = UrlImage(activity, "project")
        fun priority(activity: Activity) = UrlImage(activity, "priority")
        fun issueType(activity: Activity) = UrlImage(activity, "issueType")
    }

    fun String.md5(): String {
        val bytes = MessageDigest.getInstance("MD5").apply {
                update(toByteArray(), 0, length)
            }.digest()

        return BigInteger(1, bytes).toString(16)
    }
}