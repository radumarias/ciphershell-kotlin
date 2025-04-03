package rs.xor.rencfs.krencfs.data.sqldelight.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.net.toUri
import rs.xor.rencfs.krencfs.utils.UrlLauncher

class AndroidUrlLauncher(private val context: Context) : UrlLauncher {
    override fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        try {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            } else {
                Log.e("AndroidUrlLauncher", "No activity found to handle URL: $url")
            }
        } catch (e: Exception) {
            Log.e("AndroidUrlLauncher", "Failed to open URL: $url", e)
        }
    }
}
