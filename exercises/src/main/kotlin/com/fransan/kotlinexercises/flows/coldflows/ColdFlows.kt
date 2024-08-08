package com.fransan.kotlinexercises.flows.coldflows

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

fun createValues(): Flow<Int> {
    return flow {
        log("Emitting 1")
        emit(1)
        delay(1.seconds)
        log("Emitting 2")
        emit(2)
        delay(1.seconds)
        log("Emitting 3")
        emit(3)
    }
}

val infiniteFlow = flow {
    log("infiniteFlow called")
    var count = 0
    while (true) {
        emit(count++)
        delay(200.milliseconds)
    }
}

// Slow getRandomNumber function
suspend fun getRandomNumber(): Int {
    delay(500.milliseconds)
    return Random.nextInt()
}

// Cold flows cannot emit from different coroutines
val brokenRandomNumbers = flow {
    coroutineScope {
        repeat(10) {
            launch { emit(getRandomNumber()) }
        }
    }
}

// Channel flows allow you to emit from different coroutines
val channelFlowRandomNumbers = channelFlow {
    repeat(10) {
        launch { send(getRandomNumber()) }
    }
}


fun main(): Unit = runBlocking {
    println("################")

    val myFlowOfValues = createValues()
    // Nothing is computed until we call collect()
    myFlowOfValues.collect { log(it) }

    println("################")

    coroutineScope {
        launch {
            // Each collect trigger to call the function in the ColdFlow
            infiniteFlow
                .takeWhile { it < 10 }
                .collect { log("[Collector 1] $it") }
        }

        launch {
            // Each collect trigger to call the function in the ColdFlow
            infiniteFlow
                .takeWhile { it < 10 }
                .collect { log("[Collector 2] $it") }
        }
    }

    println("################")

    channelFlowRandomNumbers.collect { log("[ChannelFlow 1] $it") }

}