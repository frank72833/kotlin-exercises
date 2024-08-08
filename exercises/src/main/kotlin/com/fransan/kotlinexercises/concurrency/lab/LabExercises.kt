package com.fransan.kotlinexercises.concurrency.lab

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class Task(
    val description: String,
    val waitingTime: Long?,
    val runningTime: Long?,
) {
    ORDER_DOORS("order doors", 750L, null),
    ORDER_WINDOWS("order windows", 1_500L, null),
    INSTALL_DOORS("install doors", null, 1_000L),
    INSTALL_WINDOWS("install windows", null, 1_000L),
    INSTALL_BRICKS("install bricks", null, 1_000L),
}

suspend fun executeTask(task: Task): Task {
    log("Executing task start: ${task.description}")
    task.waitingTime?.let { delay(it) }
    task.runningTime?.let { Thread.sleep(it) }
    log("Executing task end: ${task.description}")
    return task
}

suspend fun installWindows() = coroutineScope {
    coroutineScope {
        launch(Dispatchers.IO) {
            executeTask(Task.ORDER_WINDOWS)
        }
    }

    coroutineScope {
        launch {
            executeTask(Task.INSTALL_WINDOWS)
        }
    }
}

suspend fun installDoors() = coroutineScope {
    coroutineScope {
        launch(Dispatchers.IO) {
            executeTask(Task.ORDER_DOORS)
        }
    }

    coroutineScope {
        launch {
            executeTask(Task.INSTALL_DOORS)
        }
    }
}

suspend fun layingBricks() = coroutineScope {
    launch {
        executeTask(Task.INSTALL_BRICKS)
    }
}

fun main(): Unit = runBlocking {
    val start = System.currentTimeMillis()

    coroutineScope {
        launch(Dispatchers.Default) { installWindows() }
        launch(Dispatchers.Default) { installDoors() }
        launch(Dispatchers.Default) { layingBricks() }
    }

    println("Total duration: ${System.currentTimeMillis() - start}ms")
}