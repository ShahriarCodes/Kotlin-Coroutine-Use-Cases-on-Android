package com.lukaslechner.coroutineusecasesonandroid.playground.coroutinebuilders

import kotlinx.coroutines.*

fun main() {
    example4()
}

fun example4() = runBlocking<Unit> {
    // lazily initialize the job
    val job = launch(start = CoroutineStart.LAZY) {
        networkRequest()
        println("result received")
    }

    delay(500)
    // wait for the job to complete
    job.start() // breakpoint

    println("end of runBlocking")
}

fun example3() = runBlocking<Unit> {
    val job = launch {
        networkRequest()
        println("result received")
    }

    // wait for the job to complete
    job.join()

    println("end of runBlocking")
}

suspend fun networkRequest(): String {
    delay(1000)
    return "result"
}

fun example2() = runBlocking<Unit> {
    // runBlocking is used to behave as blocking scope which let the coroutine to finish and print

    launch {
        delay(500)
        println("printed from within Coroutine")
    }

    println("main ends")
}

fun example1() {
    // launch non blocking function which only gets initialized
    GlobalScope.launch {
        delay(500)
        println("printed from within Coroutine")
    }

//    use a sleep time to let the non blocking launch to get finished
    Thread.sleep(1000)
    println("main ends")
}

