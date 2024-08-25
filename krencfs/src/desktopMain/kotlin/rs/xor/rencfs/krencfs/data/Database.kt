package rs.xor.rencfs.krencfs.data

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File

actual suspend fun provideSQLDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String,
    databaseFileName: String,
): SqlDriver = JdbcSqliteDriver("jdbc:sqlite:$databaseFileName")
        .apply {
            println("provideSQLDriver: $this")
            if (!File(databaseFileName).exists()) {
                schema.awaitCreate(this)
            }
        }
