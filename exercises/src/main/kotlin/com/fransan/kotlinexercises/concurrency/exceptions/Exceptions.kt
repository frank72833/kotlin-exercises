package com.fransan.kotlinexercises.concurrency.exceptions

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlinx.coroutines.yield
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

suspend fun doWork() {
    delay(500.milliseconds)
    throw UnsupportedOperationException("Didn't work!")
}

// Cannot catch CancellationException, IllegalStateException, RuntimeException, Exception or Throwable
// due to potentially missing signaling such as a cancelled coroutine ending up in infinite loops
suspend fun catchingTooGenericException() = coroutineScope {
    withTimeoutOrNull(2.seconds) {
        while(true) {
            try {
                doWork()
            } catch (e: Exception) {
                println("Oops: ${e.message}")
            }
        }
    }
}

suspend fun nonCancellableWork(): Int {
    log("Doing heavy work")
    var counter = 0
    // Increase counter during 500ms
    val start = System.currentTimeMillis()
    while(System.currentTimeMillis() < start + 500) {
        counter++
    }
    return counter
}

suspend fun cancelNonCancellationPoint() = coroutineScope {
    val myJob = launch {
        repeat(5) {
            nonCancellableWork()
            // We can add a check to see if the job is still active
            try {
                ensureActive()
            } catch (e: Exception) {
                println("Cancelled? ${e.message}")
                throw e
            }
        }
    }

    // It should print twice (0ms, 500ms) but it prints 5 times if not ensureActive()
    delay(600.milliseconds)
    log("Cancelling...")
    myJob.cancel()
}

// We need here Default dispatcher to use a pool of multiple threads
// Otherwise cancel will be never called since the single thread is occupied by the nonCancellableWork()
fun main() = runBlocking(Dispatchers.Default) {
    log("Main function start")

    // catchingTooGenericException() // Infinite loop
    cancelNonCancellationPoint()

    log("Main function end")
}