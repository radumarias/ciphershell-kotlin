package rs.xor.rencfs.krencfs.screen.walkthrough.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

actual class RecoveryCodeSaver {
    actual suspend fun saveRecoveryCode(recoveryCode: String, fileName: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val homeDir = System.getProperty("user.home")
            val file = File(homeDir, fileName)
            file.writeText(recoveryCode)
            Result.success(Unit)
        } catch (e: IOException) {
            Result.failure(e)
        }
    }
}

actual fun provideRecoveryCodeSaver(): RecoveryCodeSaver = RecoveryCodeSaver()
