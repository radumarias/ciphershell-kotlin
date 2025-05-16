package rs.xor.rencfs.krencfs.screen.walkthrough

expect class RecoveryCodeSaver {
    suspend fun saveRecoveryCode(recoveryCode: String, fileName: String): Result<Unit>
}

expect fun provideRecoveryCodeSaver(): RecoveryCodeSaver