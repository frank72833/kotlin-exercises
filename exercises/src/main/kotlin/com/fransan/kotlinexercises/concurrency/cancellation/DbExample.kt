package com.fransan.kotlinexercises.concurrency.cancellation

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

class DbConnection: AutoCloseable {
    fun write(s: String) = log("Writing: $s")
    override fun close() {
        log("Closing!")
    }
}

fun main() {
    runBlocking {
        val dbTask = launch {
            DbConnection().use { db ->
                delay(500.milliseconds)
                db.write("Hello there!")
            }
        }

        delay(200.milliseconds)
        dbTask.cancel()
    }
}