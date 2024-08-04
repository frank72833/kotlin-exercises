package com.fransan.kotlinexercises.generics

import java.math.BigDecimal

fun <T: Any> functionA(input: T): String =
    input.toString()

inline fun <reified T> functionB(input: String): Boolean = input is T

inline fun <reified T> List<Any?>.filterInstanceOf(): List<Any?> =
    this.filter { it is T }

fun main() {
    println("##########")
    println(functionA("ASD"))

    // functionA(null) // Does not compile
    println("##########")

    println(functionB<BigDecimal>("ad"))

    println("##########")
    val list = listOf("ASD", 1L, 2)
    val filteredList = list.filterInstanceOf<Long>()
    println(filteredList)
}