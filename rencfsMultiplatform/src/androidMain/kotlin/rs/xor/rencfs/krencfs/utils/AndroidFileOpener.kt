package rs.xor.rencfs.krencfs.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AndroidFileOpener :
    FileOpener,
    KoinComponent {
    private val context: Context by inject()

    override fun openDirectory(uri: String) {
        try {
            if (!uri.startsWith("content://")) {
                throw IllegalArgumentException("Invalid URI: Must be a content:// URI")
            }

            val parsedUri = uri.toUri()
            val documentFile = DocumentFile.fromTreeUri(context, parsedUri)
            if (documentFile == null || !documentFile.exists() || !documentFile.isDirectory) {
                throw IllegalArgumentException("Invalid or non-existent directory: $uri")
            }

            context.contentResolver.takePersistableUriPermission(
                parsedUri,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(parsedUri, "vnd.android.document/directory")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            try {
                context.startActivity(intent)
            } catch (e: android.content.ActivityNotFoundException) {
                val fallbackIntent = Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(parsedUri, "*/*")
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(fallbackIntent)
            }
        } catch (e: Exception) {
            throw RuntimeException("Failed to open directory: ${e.message}", e)
        }
    }
}
