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

    val driver: SqlDriver = try {
        JdbcSqliteDriver("jdbc:sqlite:$dbPath", Properties())
    } catch (e: Exception) {
        throw RuntimeException("Failed to create SQLite driver: ${e.message}", e)
    }

    val currentVersion = driver.executeQuery(
        identifier = null,
        sql = "PRAGMA user_version;",
        mapper = { cursor -> QueryResult.Value(cursor.getLong(0)?.toInt() ?: 0) },
        parameters = 0,
    ).value
    println("Current database version: $currentVersion")

    val targetVersion = 2

    if (currentVersion == 0) {
        println("Creating new database schema (version $targetVersion)")
        schema.create(driver).await()
        driver.execute(null, "PRAGMA user_version = $targetVersion", 0).await()
    } else if (currentVersion < targetVersion) {
        println("Migrating database from version $currentVersion to $targetVersion")

        val vaultTableExists = try {
            driver.executeQuery(
                identifier = null,
                sql = "SELECT 1 FROM Vault LIMIT 1",
                mapper = { _ -> QueryResult.Value(true) },
                parameters = 0,
            ).value
        } catch (e: Exception) {
            false
        }

        if (!vaultTableExists && currentVersion == 1) {
            println("Vault table missing at version 1; creating minimal table")
            driver.execute(
                null,
                """
                CREATE TABLE Vault (
                    `id` INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    path TEXT NOT NULL,
                    mount TEXT NOT NULL
                )
            """,
                0,
            ).await()
        }
        schema.migrate(
            driver,
            oldVersion = currentVersion.toLong(),
            newVersion = targetVersion.toLong(),
        ).await()
    }

    return driver
}
