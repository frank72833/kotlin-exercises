package com.fransan.kotlinexercises.generics

open class Animal {
    fun feed() = println("Animal feed")
}

class Cat: Animal() {
    fun cleanLitter() { println("Cat clean litter") }
}

// out means covariant so any subtype can be use instead of this class
// Herd<Animal>
// Herd<Cat> subtype because Cat is a subtype of Animal
class Herd<out T: Animal>(vararg animals: T) {
    private val _animals = animals

    val size: Int get() = _animals.size
    operator fun get(i: Int): T { return _animals[i] }
}

fun feedAll(animals: Herd<Animal>) {
    for(i in 0..<animals.size) {
        animals[i].feed()
    }
}

fun takeCareOfCats(cats: Herd<Cat>) {
    for(i in 0..<cats.size) {
        cats[i].cleanLitter()
    }
    feedAll(cats) // If not covariant (out) this won't work
}

fun main() {
    val catHerd = Herd(Cat(), Cat(), Cat())
    takeCareOfCats(catHerd)
}