import kotlinx.coroutines.*
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

fun main(args: Array<String>) {
    //exampleBlocking()
   // exampleCustomLunchCorutineScope()
    exampleAsyncAwait()
}


suspend fun printlnDelayed(message: String){
    delay(1000)
    //println(Thread.currentThread())
    println(message)
}


fun exampleBlocking() = runBlocking{
    println("one")
    printlnDelayed("two")
    println("three")
}

fun exampleBlockingDispatcher(){
    runBlocking (Dispatchers.Default){
        println("one - from thread ${Thread.currentThread().name}")
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }

    println("three - from thread ${Thread.currentThread().name}")
}

fun exampleLunchGlobal() = runBlocking{
    println("one - from thread ${Thread.currentThread().name}")
    GlobalScope.launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")
    delay(3000)
}


fun exampleLunchGlobalWaiting() = runBlocking{
    println("one - from thread ${Thread.currentThread().name}")
    val job = GlobalScope.launch {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")
    job.join()
}

fun exampleLunchCorutineScope() = runBlocking{
    println("one - from thread ${Thread.currentThread().name}")


    launch(Dispatchers.IO) {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")

}

suspend fun calculatingHardThings(startNum: Int): Int{
    delay(1000)
    return startNum * 10
}

fun exampleCustomLunchCorutineScope() = runBlocking{
    println("one - from thread ${Thread.currentThread().name}")

    val customDispather = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

    launch(customDispather) {
        printlnDelayed("two - from thread ${Thread.currentThread().name}")
    }
    println("three - from thread ${Thread.currentThread().name}")

    (customDispather.executor as ExecutorService).shutdown()

}

fun exampleAsyncAwait() = runBlocking {
    val startTime = System.currentTimeMillis()

    val deferred1 = async { calculatingHardThings(10) }
    val deferred2 = async { calculatingHardThings(20) }
    val deferred3 = async { calculatingHardThings(30) }

    val sum = deferred1.await() + deferred2.await() + deferred3.await()
    println("async/await result = $sum")

    val endTime = System.currentTimeMillis()
    println("time taken: ${endTime - startTime}")

}

fun exampleWithContext() = runBlocking {
    val startTime = System.currentTimeMillis()

    val result1 = withContext(Dispatchers.Default) { calculatingHardThings(10) }
    val result2 = withContext(Dispatchers.Default) { calculatingHardThings(20) }
    val result3 = withContext(Dispatchers.Default) { calculatingHardThings(30) }

    val sum = result1 + result2 + result3
    println("async/await result = $sum")

    val endTime = System.currentTimeMillis()
    println("time taken: ${endTime - startTime}")

}