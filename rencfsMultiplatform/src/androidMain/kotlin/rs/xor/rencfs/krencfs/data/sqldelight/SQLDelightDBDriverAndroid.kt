package rs.xor.rencfs.krencfs.data.sqldelight

import android.content.Context
import app.cash.sqldelight.async.coroutines.awaitCreate
import app.cash.sqldelight.async.coroutines.synchronous
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import rs.xor.rencfs.krencfs.RootContextProvider
import java.io.File

lateinit var context: Context

actual suspend fun provideSQLDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>,
    databaseName: String,
    databaseFileName: String,
): SqlDriver = AndroidSqliteDriver(schema.synchronous(), RootContextProvider.getContext(), databaseFileName)
