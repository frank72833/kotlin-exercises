package com.fransan.kotlinexercises.flows.hotflows.sharedflows

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class RadioStation {
    // Replay param can be set to indicate a cache with the latest N events to be stored for new subscribers
    private val _messageFlow = MutableSharedFlow<Int>(replay = 5)
    val messageFlow = _messageFlow.asSharedFlow()

    // Fire and forget coroutine passing CoroutineScope
    fun startBroadcasting(scope: CoroutineScope): Job =
        scope.launch {
            log(coroutineContext)
            while (true) {
                delay(500.milliseconds)
                val number = Random.nextInt(0,10)
                log("Emitting number: $number")
                _messageFlow.emit(number)
            }
        }
    }

fun RadioStation.startSubscribers(scope: CoroutineScope, number: Int) : Job =
    scope.launch {
        repeat(number) { time ->
            // Launching many coroutines each with one subscriber
            launch {
                messageFlow.collect { number ->
                    log("[Collector: $time] Received number: $number")
                }
            }
        }
    }

fun querySensor(): Int = Random.nextInt(-100, 100)

fun getTemperature(): Flow<Int> {
    return flow {
        while(true) {
            emit(querySensor())
            delay(500.milliseconds)
        }
    }
}

fun celsiusToFahrenheit(c: Int) = c * 9.0 / 5.0 + 32.0

fun main() : Unit = runBlocking {
    println("##########################")
    val radio = RadioStation()
    val broadcastingJob = radio.startBroadcasting(this)

    delay(600.milliseconds)
    val subscriberJobs = radio.startSubscribers(this, 2)
    subscriberJobs.children

    delay(2.seconds)
    subscriberJobs.cancel()
    delay(2.seconds)
    broadcastingJob.cancel()

    println("##########################")
    // Cold Flows to Shared (hot) Flow
    val temps = getTemperature()
    val sharedTemps = temps.shareIn(this, SharingStarted.Lazily)
    launch {
        sharedTemps.collect {
            log("$it Celsius")
        }
    }

    launch {
        sharedTemps.collect {
            log("${celsiusToFahrenheit(it)} Fahrenheit")
        }
    }

    println("#!@!WD1ef12rr12")
}