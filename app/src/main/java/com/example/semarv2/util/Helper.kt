package com.example.semarv2.util

import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import com.example.semarv2.R
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun createTempFile(context: Context): File {
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(timeStamp, ".jpg", storageDir)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (
        mediaDir != null && mediaDir.exists()
    ) mediaDir else application.filesDir

    return File(outputDirectory, "$timeStamp.jpg")
}

fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix()
    return if (isBackCamera) {
        matrix.postRotate(90f)
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        matrix.postRotate(-90f)
        matrix.postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f) // flip gambar
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}

fun uriToFile(selectedImg: Uri, context: Context): File {
    val contentResolver: ContentResolver = context.contentResolver
    val myFile = createTempFile(context)

    val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
    val outputStream: OutputStream = FileOutputStream(myFile)

    val buf = ByteArray(1024)
    var len: Int
    while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
    outputStream.close()
    inputStream.close()
    return myFile
}

fun reduceFileImg(file: File, maxSize: Int = 2000000): File {
    val bitmap = BitmapFactory.decodeFile(file.path)

    var compressQuality = 100
    var streamLength: Int

    do {
        val bmpStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
        val bmpPicByteArray = bmpStream.toByteArray()
        streamLength = bmpPicByteArray.size
        compressQuality -= 5
    } while (streamLength > maxSize)
    bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
    return file
}

//Downscale Image File
fun downscaleImage(file: File): File? {
    val targetWidth = 150
    val targetHeight = 150

    return try {
        // Decode file gambar menjadi Bitmap
        val options = BitmapFactory.Options().apply {
            inJustDecodeBounds = true
        }
        BitmapFactory.decodeFile(file.path, options)

        // Tentukan faktor skala untuk downscale
        val scaleFactor =
            calculateScaleFactor(options.outWidth, options.outHeight, targetWidth, targetHeight)

        // Konfigurasi opsi decoding untuk menghasilkan gambar downscale
        val decodeOptions = BitmapFactory.Options().apply {
            inSampleSize = scaleFactor
        }

        // Decode file gambar dengan opsi downscale
        val bitmap = BitmapFactory.decodeFile(file.path, decodeOptions)

        // Mengubah ukuran bitmap menjadi 150x150 piksel
        val scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true)
        bitmap.recycle() // Hapus bitmap asli karena kita hanya tertarik dengan yang diubah ukuran

        // Membuat file untuk menyimpan gambar hasil downscale
        val outputFile = File.createTempFile("downscaled_", ".jpg")
        val outputStream = FileOutputStream(outputFile)
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        outputFile // Mengembalikan file gambar hasil downscale
    } catch (e: Exception) {
        e.printStackTrace()
        null // Mengembalikan null jika terjadi kesalahan
    }
}

// Menghitung faktor skala untuk downscale
@Suppress("SameParameterValue")
private fun calculateScaleFactor(
    originalWidth: Int,
    originalHeight: Int,
    targetWidth: Int,
    targetHeight: Int
): Int {
    return when {
        originalWidth > originalHeight -> originalWidth / targetWidth
        originalHeight > originalWidth -> originalHeight / targetHeight
        else -> originalWidth / targetWidth
    }
}

// Format mata uang
fun String.withCurrencyFormat(): String {
    val amount = this.toDoubleOrNull() ?: 0.0
    val format = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return format.format(amount)
}