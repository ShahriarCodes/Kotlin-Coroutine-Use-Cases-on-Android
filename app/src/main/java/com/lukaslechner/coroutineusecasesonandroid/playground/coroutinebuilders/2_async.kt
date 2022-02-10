package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.*

fun main() {
    asyncExample2()
}

// avoid shared mutable state in concurrent programming
fun asyncExample2() = runBlocking {
    val startTime = System.currentTimeMillis()

    val deferred1 = async {
        val result1 = networkCall(1, 500)
        println("result received: $result1 after ${elapsedMillis(startTime)}ms")
        result1
    }
    val deferred2 = async {
        val result2 = networkCall(2, 1000)
        println("result received: $result2 after ${elapsedMillis(startTime)}ms")
        result2
    }

    // lazy initialization
    val deferred3 = async(start = CoroutineStart.LAZY) {
        val result3 = networkCall(2, 1000)
        println("result received: $result3 after ${elapsedMillis(startTime)}ms")
        result3
    }

    // deferred1.await() // get the return value
    // deferred1.join() // wait fot the deferred to finish
    // deferred1.cancel() // cancel

    val resultList = listOf(deferred1.await(), deferred2.await())

    println("result list: $resultList after ${elapsedMillis(startTime)}ms")

    deferred3.start()
    println("deffered3: ${deferred3.await()} after ${elapsedMillis(startTime)}ms")
}

fun asyncExample1() = runBlocking {
    val startTime = System.currentTimeMillis()
    val resultList = mutableListOf<String>()

    val job1 = launch {
        val result1 = networkCall(1, 500)
        resultList.add(result1) // shared mutable state
        println("result received: $result1 after ${elapsedMillis(startTime)}ms")
    }
    val job2 = launch {
        val result2 = networkCall(3, 1000)
        resultList.add(result2) // shared mutable state
        println("result received: $result2 after ${elapsedMillis(startTime)}ms")
    }

    // wait until both coroutines completes
    job1.join()
    job2.join()

    println("result list: $resultList after ${elapsedMillis(startTime)}ms")
}

suspend fun networkCall(number: Int, delay: Long): String {
    delay(delay)
    return "Result $number"
}

fun elapsedMillis(startTime: Long) = System.currentTimeMillis() - startTime