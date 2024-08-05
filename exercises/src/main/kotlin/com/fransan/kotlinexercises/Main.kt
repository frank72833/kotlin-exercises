package com.fransan.kotlinexercises

import java.time.Duration
import java.time.Instant

private val zeroTime = Instant.now()

fun log(message: Any?) =
    println("${Duration.between(zeroTime, Instant.now()).toMillis()}ms " + "[${Thread.currentThread().name}] $message")

fun main(args: Array<String>) {
    println("Hello World!")

    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.
    println("Program arguments: ${args.joinToString()}")
}