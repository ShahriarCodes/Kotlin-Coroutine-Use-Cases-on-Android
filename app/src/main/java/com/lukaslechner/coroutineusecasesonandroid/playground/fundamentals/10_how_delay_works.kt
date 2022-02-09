package com.lukaslechner.coroutineusecasesonandroid.playground.fundamentals

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.async
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.runBlocking

//fun main() = runBlocking {
//    println("main starts")
//    joinAll(
//        async { delayDemonstration(1, 5000) },
//        async { delayDemonstration(2, 3000) },
//    )
//    println("main ends")
//}

fun main()  {
    Handler(Looper.getMainLooper())
        .postDelayed(Runnable {
            println("Coroutine $2 has finished")
        }, 1000)
}

suspend fun delayDemonstration(number: Int, delay: Long) {
    println("Coroutine $number starts work on ${Thread.currentThread().name}")
    // delay(delay)

    Handler(Looper.getMainLooper())
        .postDelayed(Runnable {
            println("Coroutine $number has finished")
        }, delay)
}