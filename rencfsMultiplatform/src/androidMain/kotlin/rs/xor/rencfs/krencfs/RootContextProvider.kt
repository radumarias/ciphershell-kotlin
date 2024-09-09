package rs.xor.rencfs.krencfs

import android.content.Context
import kotlinx.coroutines.CompletableDeferred

object RootContextProvider {
    private val contextDeferred = CompletableDeferred<Context>()

    fun initialize(context: Context) {
        if (!contextDeferred.isCompleted) {
            contextDeferred.complete(context)
        }
    }

    // Suspend function to get the context, which will wait until the context is set
    suspend fun getContext(): Context = contextDeferred.await()
}
