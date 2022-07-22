package apply.infra.throttle

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class RequestPerSecondLimiter(
    private val permitsPerSecond: Int,
    private val requestTimes: Queue<Long> = ConcurrentLinkedQueue()
) {
    fun isExceed(requestTime: Long = System.currentTimeMillis()): Boolean = !tryAcquire(requestTime)

    fun tryAcquire(requestTime: Long = System.currentTimeMillis()): Boolean {
        sync(requestTime)
        return if (requestTimes.size < permitsPerSecond) {
            requestTimes.offer(requestTime)
            true
        } else {
            false
        }
    }

    private fun sync(requestTime: Long) {
        while (requestTimes.isNotEmpty()) {
            val elapsedTime = requestTime - requestTimes.peek()
            if (elapsedTime.milliseconds < 1.seconds) {
                break
            }
            requestTimes.poll()
        }
    }
}
