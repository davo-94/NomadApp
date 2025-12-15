package cl.vasquez.nomadapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import kotlin.math.max

object ImageCompressionUtils {

    fun compress(
        context: Context,
        uri: Uri,
        maxSize: Int = 1280,
        quality: Int = 75
    ): File {
        val input = context.contentResolver.openInputStream(uri)!!
        val original = BitmapFactory.decodeStream(input)
        input.close()

        val scale = max(
            original.width.toFloat() / maxSize,
            original.height.toFloat() / maxSize
        ).coerceAtLeast(1f)

        val resized = Bitmap.createScaledBitmap(
            original,
            (original.width / scale).toInt(),
            (original.height / scale).toInt(),
            true
        )

        val outFile = File.createTempFile("img_", ".jpg", context.cacheDir)
        FileOutputStream(outFile).use {
            resized.compress(Bitmap.CompressFormat.JPEG, quality, it)
        }

        return outFile
    }
}
