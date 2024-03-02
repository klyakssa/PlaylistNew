package com.kuzmin.playlist.presentation.utils


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun debounce(
    delayMillis: Long,
    coroutineScope: CoroutineScope,
    dispatcher: MainCoroutineDispatcher,
    function: () -> Unit
): () -> Unit {
    var debounceJob: Job? = null
    return {
        if (debounceJob?.isCompleted != false) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                withContext(dispatcher){
                    function()
                }
            }
        }
    }
}
fun <T> debounce(delayMillis: Long,
                 coroutineScope: CoroutineScope,
                 dispatcher: MainCoroutineDispatcher,
                 action: (T) -> Unit): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob?.isCompleted != false) {
            debounceJob = coroutineScope.launch {
                delay(delayMillis)
                withContext(dispatcher){
                    action(param)
                }
            }
        }
    }
}