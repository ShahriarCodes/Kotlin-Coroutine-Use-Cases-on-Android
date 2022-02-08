package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import kotlinx.coroutines.*

fun main() = runBlocking {
    repeat(1_000_000) {
        launch {
            delay(5000)
            print(".")
        }
    }
}
