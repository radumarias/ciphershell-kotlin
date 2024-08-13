package rs.xor.rencfs.krencfs.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.db.SqlSchema
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import rs.xor.rencfs.krencfs.KrenkfsDB
import rs.xor.rencfs.krencfs.VaultsQueries

expect suspend fun provideSQLDriver(
    schema: SqlSchema<QueryResult.AsyncValue<Unit>>
): SqlDriver

object Database {
    /* Dummy for now */
    fun initialize(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            val driver = provideSQLDriver(KrenkfsDB.Schema)
            val vaults = VaultsQueries(driver)
            val rows = vaults.selectAll()
                .asFlow()
                .mapToList(Dispatchers.IO)
        }
    }
}