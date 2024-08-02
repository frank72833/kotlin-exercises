package com.fransan.kotlinexercises.delegate

class Person {
    private val _attributes = mutableMapOf<String, String>()

    var name: String by _attributes

    fun setAttribute(attrName: String, value: String): Unit {
        _attributes[attrName] = value
    }

    override fun toString(): String =
        _attributes.toString()
}

fun main() {
    val person = Person()
    person.name = "fran"
    person.setAttribute("surname", "sanchez")
    println(person)
}