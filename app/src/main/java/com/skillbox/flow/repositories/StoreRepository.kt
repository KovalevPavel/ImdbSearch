package com.skillbox.flow.repositories

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.skillbox.flow.data.Movie
import com.skillbox.flow.network.Network
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StoreRepository(private val context: Context) {
    suspend fun savePoster(movie: Movie): String? {
        movie.posterExternalUrl ?: return null
        return withContext(Dispatchers.IO) {
            val posterExtension =
                Uri.parse(movie.posterExternalUrl).lastPathSegment?.substringAfterLast('.')
            val fileName = "${movie._id}.$posterExtension"
            val posterFile = savePosterDetails(fileName)
            savePosterOnDevice(movie.posterExternalUrl, posterFile)
            posterFile.toUri().toString()
        }
    }

    private suspend fun savePosterOnDevice(posterUrl: String, file: File) {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            Network.api.downloadPoster(posterUrl).byteStream()
                .use { inputStream ->
                    inputStream.copyTo(file.outputStream())
                }
        }
    }

    private fun savePosterDetails(name: String): File {
        val volume =
            if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES
            ) else context.filesDir
        return File(volume, name)
    }
}
