package apply.infra.throttle

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

private const val ONE_SECOND = 1_000

class RequestPerSecondLimiter(
    private val permitsPerSecond: Int
) {
    private val requestTimes: Queue<Long> = ConcurrentLinkedQueue()

    fun isExceed(requestTime: Long = System.currentTimeMillis()): Boolean = !tryAcquire(requestTime)

    fun tryAcquire(requestTime: Long = System.currentTimeMillis()): Boolean {
        sync(requestTime)

        val acquirable = requestTimes.size < permitsPerSecond
        if (acquirable) {
            requestTimes.offer(requestTime)
            return true
        }

        return false
    }

    private fun sync(requestTime: Long) {
        while (requestTimes.isNotEmpty()) {
            val elapsedTime = requestTime - requestTimes.peek()
            val withinOneSecond = elapsedTime < ONE_SECOND
            if (withinOneSecond) {
                break
            }

            requestTimes.poll()
        }
    }
}
