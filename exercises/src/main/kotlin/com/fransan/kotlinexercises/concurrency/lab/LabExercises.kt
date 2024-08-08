package com.fransan.kotlinexercises.concurrency.lab

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

enum class Product(
    val description: String,
    val deliveryTime: Long,
) {
    DOORS("doors", 750L),
    WINDOWS("windows", 1_500L)
}

suspend fun order(products: Product): Product {
    println("Ordering: ${products.description}")
    delay(products.deliveryTime)
    println("Order delivered: ${products.description}")
    return products
}

fun perform(taskName: String): String {
    println("Working on: $taskName")
    Thread.sleep(1_000) // Cannot be suspended
    println("Completed: $taskName")
    return taskName
}

fun installWindows(scope: CoroutineScope) {
    val order = scope.async(Dispatchers.IO) {
        order(Product.WINDOWS)
    }

    scope.launch(Dispatchers.Default) {
        order.await() // Wait for order to be completed
        perform("Installing windows")
    }
}

fun installDoors(scope: CoroutineScope) {
    val order = scope.async(Dispatchers.IO) {
        order(Product.DOORS)
    }

    scope.launch(Dispatchers.Default) {
        order.await() // Wait for order to be completed
        perform("Installing doors")
    }
}

fun layingBricks(scope: CoroutineScope) =
    scope.launch(Dispatchers.Default) {
        perform("laying bricks")
    }

fun main(): Unit = runBlocking {
    val start = System.currentTimeMillis()

    coroutineScope {
        installWindows(this)
        installDoors(this)
        layingBricks(this)
    }

    println("Total duration: ${System.currentTimeMillis() - start}ms")
}