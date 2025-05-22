package rs.xor.rencfs.krencfs.screen.walkthrough.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.io.IOException

actual class RecoveryCodeSaver {
    actual suspend fun saveRecoveryCode(recoveryCode: String, fileName: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            object : KoinComponent {
                val context: Context by inject()
            }.apply {
                val file = File(context.filesDir, fileName)
                file.writeText(recoveryCode)
            }
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}

actual fun provideRecoveryCodeSaver(): RecoveryCodeSaver = RecoveryCodeSaver()
