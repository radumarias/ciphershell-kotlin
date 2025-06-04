package rs.xor.rencfs.krencfs.data.sqldelight

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import java.util.Properties

actual suspend fun provideSQLDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String,
    databaseFileName: String,
): SqlDriver {
    val homeDir = System.getProperty("user.home")
    val dbDir = File(homeDir, ".krenkfs")
    if (!dbDir.exists()) {
        dbDir.mkdirs()
    }
    val dbPath = File(dbDir, databaseFileName).absolutePath
    println("provideSQLDriver: Database path: $dbPath")

    // Delete existing database to enforce version 1
    if (File(dbPath).exists()) {
        println("Deleting existing database at $dbPath")
        File(dbPath).delete()
    }

    val driver: SqlDriver = try {
        JdbcSqliteDriver("jdbc:sqlite:$dbPath", Properties())
    } catch (e: Exception) {
        throw RuntimeException("Failed to create SQLite driver: ${e.message}", e)
    }

    val currentVersion = driver.executeQuery(
        identifier = null,
        sql = "PRAGMA user_version;",
        mapper = { cursor -> QueryResult.Value(cursor.getLong(0)?.toInt() ?: 0) },
        parameters = 0
    ).value
    println("Current database version: $currentVersion")

    val targetVersion = 1

    if (currentVersion == 0) {
        println("Creating new database schema (version $targetVersion)")
        schema.create(driver).await()
        driver.execute(null, "PRAGMA user_version = $targetVersion", 0).await()
    }

    return driver
}

