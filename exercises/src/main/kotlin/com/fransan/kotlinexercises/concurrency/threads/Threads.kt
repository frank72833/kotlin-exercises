package com.fransan.kotlinexercises.concurrency.threads

import kotlin.concurrent.thread

fun main() {
    println("${Thread.currentThread().name}: Hello")
    thread {
        println("${Thread.currentThread().name}: Hello there")
    }
}