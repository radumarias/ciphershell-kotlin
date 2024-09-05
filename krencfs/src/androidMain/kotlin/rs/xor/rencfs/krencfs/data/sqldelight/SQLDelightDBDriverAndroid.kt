package rs.xor.rencfs.krencfs.data.sqldelight

import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import java.io.File

actual suspend fun provideSQLDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String,
    databaseFileName: String,
): SqlDriver = AndroidSqliteDriver(schema.synchronous(), TODO("Acquire Android Context"), databaseFileName)
        .apply {
            println("provideSQLDriver: $this")
            if (!File(databaseFileName).exists()) {
                schema.awaitCreate(this)
            }
        }
