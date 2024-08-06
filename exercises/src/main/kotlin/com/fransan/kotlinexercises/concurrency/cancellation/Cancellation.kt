package com.fransan.kotlinexercises.concurrency.cancellation

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

suspend fun cancellingCoroutines() = coroutineScope {
    val launchedJob = launch {
        log("[Launch] Start!")
        delay(5000.milliseconds)
        log("[Launch] Done!")
    }
    val asyncDeferred = async {
        log("[Async] Start!")
        delay(5000.milliseconds)
        log("[Async] Done!")
    }

    delay(200.milliseconds)
    launchedJob.cancel()
    asyncDeferred.cancel()
}

suspend fun calculateResult(): Int {
    log("Calculating result...")
    delay(500.milliseconds)
    return 5 + 5
}

// CoroutineScope: Fire and forget
// coroutineScope {}: Waits for completion
fun CoroutineScope.nestedCoroutines(): Job {
    log(coroutineContext)
    return launch {
        launch {
            launch {
                launch {
                    log("Nested coroutines start")
                    delay(5.seconds)
                    log("Nested coroutines end")
                }
            }
        }
    }
}

fun main() = runBlocking(Dispatchers.Default) {
    println("####################")

    cancellingCoroutines()

    println("####################")

    val resultOne = withTimeoutOrNull(100.milliseconds) {
        calculateResult()
    }
    println(resultOne)

    val resultTwo = withTimeoutOrNull(600.milliseconds) {
        calculateResult()
    }
    println(resultTwo)

    println("####################")
    val job = nestedCoroutines()
    delay(1.seconds)
    job.cancel()

    log("Main function end")
}