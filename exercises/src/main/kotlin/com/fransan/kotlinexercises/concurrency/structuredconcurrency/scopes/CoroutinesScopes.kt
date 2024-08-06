package com.fransan.kotlinexercises.concurrency.structuredconcurrency.scopes

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.coroutineContext
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

suspend fun generateValue(): Int {
    log(coroutineContext)
    delay(500.milliseconds)
    return Random.nextInt(0, 100)
}

suspend fun computeSum() {
    log(coroutineContext)
    log("Computing sum...")
    val sum = coroutineScope {
        val a = async { generateValue() }
        val b = async { generateValue() }
        a.await() + b.await()
    }
    log("Sum is $sum")
}


fun main() = runBlocking(Dispatchers.Default) {
    log(coroutineContext)
    computeSum()
}