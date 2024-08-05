package com.fransan.kotlinexercises.concurrency.suspend

import com.fransan.kotlinexercises.log
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.random.Random

@JvmInline
value class UserId(val value: String)

data class Credential(
    val username: String,
    val password: String
)

data class UserData(
    val name: String,
    val surname: String,
    val age: Int
)

private fun randomName(name: String) = name + Random.nextInt(0, 100)
private fun randomAge() = Random.nextInt(0, 100)

suspend fun login(credential: Credential): UserId {
    delay(2000)
    log("User logged in: ${credential.username}")
    return UserId(randomName("USER-"))
}

suspend fun loadUserData(userId: UserId): UserData {
    delay(2000)
    log("User data loaded: ${userId.value}")
    return UserData(
        name = randomName("Fran"),
        surname = randomName("Sanchez"),
        age = randomAge()
    )
}

fun showData(userData: UserData) {
    log("User data: $userData")
}

suspend fun showUserInfo(credential: Credential): UserData {
    val userId = login(credential)
    val userData = loadUserData(userId)
    showData(userData)

    return userData
}

suspend fun executeAsync(): List<UserData> = coroutineScope {
    // Async returns a deferred object where we can fetch results
    val deferredOne = async {
        showUserInfo(
            Credential(
                username = "username1",
                password = "12qwio"
            )
        )
    }

    val deferredTwo = async {
        showUserInfo(
            Credential(
                username = "username2",
                password = "543fd"
            )
        )
    }

    // Blocking call to wait for results
    val userDataOne = deferredOne.await()
    val userDataTwo = deferredTwo.await()

    listOf(userDataOne, userDataTwo)
}


fun main(): Unit = runBlocking {
    val result = executeAsync()
    println(result)
}