package com.fransan.kotlinexercises.concurrency.cancellation

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

suspend fun nonCancellableWork(): Int {
    log("Doing heavy work")
    var counter = 0
    // Increase counter during 500ms
    val start = System.currentTimeMillis()
    while(System.currentTimeMillis() < start + 500) {
        counter++
        // Allow other coroutines to run (suspension point)
        yield()
    }
    return counter
}

fun main(): Unit = runBlocking {
    log("Main function start")

    launch {
        repeat(3) {
            nonCancellableWork()
        }
    }

    launch {
        repeat(3) {
            nonCancellableWork()
        }
    }

    log("Main function end")
}