package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("main starts")
    joinAll(
        async { suspendingCoroutine(1, 5000) },
        async { suspendingCoroutine(2, 3000) },
        async {
            repeat(10) {
                println(
                    "Other tasks working on ${Thread.currentThread().name}"
                )
                delay(500)
            }
        }
    )
    println("main ends")
}

suspend fun suspendingCoroutine(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    delay(delay)
    println("Coroutine $number has finished")
}