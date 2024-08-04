package com.fransan.kotlinexercises.generics

sealed class Fruit {
    abstract val weight: Int
}

data class Apple(
    override val weight: Int,
    val color: String
): Fruit()

data class Orange(
    override val weight: Int,
    val juicy: Boolean
): Fruit()

fun main() {
    val weightComparator = Comparator<Fruit> { a,b -> a.weight - b.weight }

    val fruits = listOf(
        Orange(180, true),
        Apple(100, "Green")
    )

    println(fruits.sortedWith(weightComparator))


    val apple = listOf(
        Apple(100, "Red"),
        Apple(100, "Green")
    )

    println(apple.sortedBy { a -> a.color })
}