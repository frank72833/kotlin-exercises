package com.fransan.kotlinexercises.concurrency.multithread

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration.Companion.seconds

suspend fun parallelCountNoMutex() = coroutineScope {
    log(coroutineContext)
    var x = 0
    repeat(1) {
        launch(Dispatchers.IO) {
            log(coroutineContext)
            x++
        }
    }
    delay(1.seconds)
    log(x)
}

suspend fun parallelCountMutex() = coroutineScope {
    log(coroutineContext)
    var x = 0
    val mutex = Mutex()
    repeat(1) {
        launch(Dispatchers.IO) {
            log(coroutineContext)
            mutex.withLock {
                x++
            }
        }
    }
    delay(1.seconds)
    log(x)
}

fun main(): Unit = runBlocking {
    launch(Dispatchers.Default) { parallelCountNoMutex() }
    launch(Dispatchers.Default) { parallelCountMutex() }
}