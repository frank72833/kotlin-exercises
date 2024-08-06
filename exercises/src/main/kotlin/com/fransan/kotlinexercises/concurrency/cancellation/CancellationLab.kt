package com.fransan.kotlinexercises.concurrency.cancellation

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield

suspend fun doWork(): Int {
    log("doWork")
    var counter = 0
    // Increase counter during 500ms
    val start = System.currentTimeMillis()
    while(System.currentTimeMillis() < start + 1000) {
        counter += doOtherWork()
        yield()
    }
    return counter
}

private suspend fun doOtherWork(): Int {
    log("doOtherWork")
    var counter = 0
    val start = System.currentTimeMillis()
    while(System.currentTimeMillis() < start + 1000) {
        counter++;
    }

    return counter
}

fun main() = runBlocking<Unit> {
    launch {
        repeat(5) {
            doWork()
        }
    }

    launch {
        repeat(5) {
            doWork()
        }
    }
}