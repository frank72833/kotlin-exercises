package com.fransan.kotlinexercises.highorderfun

import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

// High order function: Function that takes a function as param or returns one

val sum: (Int, Int) -> Int = { x, y -> x + y }

fun twoAndThree(operation: (Int, Int) -> Int) {
    val result = operation(2, 3)
    println(result)
}

fun String.filter(predicate: (Char) -> Boolean): String {
    return buildString {
        for (char in this@filter) {
            if (predicate(char)) append(char)
        }
    }
}

fun <T> Collection<T>.joinToString(
    separator: String = ", ",
    prefix: String = "",
    postfix: String = "",
    transform: (T) -> String = { it.toString() },
): String = buildString {
    append(prefix)
    for ((index, item) in this@joinToString.withIndex()) {
        if (index > 0) {
            append(separator)
        }

        append(transform(item))

    }
    append(postfix)
}

fun getCalculator(
    operation: String
): (a: Int, b: Int) -> Int =
    when(operation) {
        "sum" -> { a, b -> a + b }
        "product" -> { a, b -> a * b }
        else -> { a, b -> a + b }
}

// inline make the compiler replace where this is called with the actual code making it more performance
// inline functions cannot be stored, they need to be replaceable by the compiler
inline fun <T> synchronized(lock: Lock, action: () -> T): T {
    lock.lock()
    try {
        return action()
    } finally {
        lock.unlock()
    }
}

fun main() {
    println("############")

    println(sum(5, 1))
    twoAndThree { a, b -> a * b }

    println("############")

    println("1ab2yz3".filter { a -> a !in 'a'..'z' })

    println("############")

    println(
        listOf("a", "B", "C").joinToString(
            prefix = "$",
            separator = "#",
            postfix = "/",
            transform = { it.uppercase() }
        )
    )

    println("############")

    val calculator = getCalculator("sum")
    println(calculator(1, 2))

    println("############")

    val mutableList = mutableListOf("A")
    val lock = ReentrantLock()
    synchronized(lock) {
        mutableList.add("B")
    }

    println(mutableList)

    println("############")
}