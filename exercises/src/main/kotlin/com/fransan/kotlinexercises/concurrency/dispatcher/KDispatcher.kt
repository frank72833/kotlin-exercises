package com.fransan.kotlinexercises.concurrency.dispatcher

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

/*
    - Default (Dispatches.Default)
    General purpose
    Backed by Thread pools as many threads as CPU cores
    Coroutines distributed across multiple threads running in parallel

    - Main/UI Frameworks (Dispatchers.Main)
    All coroutines run in the main thread
    Good for UI or Android drawing frame
    Operations that need to be executed in a specific thread

    - API (Dispatches.IO)
    Automatically scaling pool of threads (up to 64)
    Designed for non-cpu intensive calls
    DB or external API calls
 */

suspend fun callExternalApi(input: String) {
    log(coroutineContext)
    delay(1000)
    log("Calling external API: $input...")
}

suspend fun performBackgroundOperation(): String {
    log(coroutineContext)
    delay(1000)
    log("Performing background operation")
    return "value"
}

fun main(): Unit = runBlocking {
    log("Do some work")

    launch(Dispatchers.Default) {
        val result = performBackgroundOperation()
        withContext(Dispatchers.IO) {
            callExternalApi(result)
        }
    }
}

