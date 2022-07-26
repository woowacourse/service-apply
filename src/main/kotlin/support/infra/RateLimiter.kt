package support.infra

import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class RateLimiter(
    private val permitsPerSecond: Int,
    private val available: Queue<Long> = ConcurrentLinkedQueue()
) {
    init {
        require(permitsPerSecond >= 1)
    }

    fun acquire(requestTime: Long = System.currentTimeMillis()) {
        if (!tryAcquire(requestTime)) {
            throw ExceededRequestException("허용된 요청 수를 초과했습니다.")
        }
    }

    private fun tryAcquire(requestTime: Long = System.currentTimeMillis()): Boolean {
        sync(requestTime)
        return if (available.size < permitsPerSecond) {
            available.offer(requestTime)
            true
        } else {
            false
        }
    }

    private fun sync(requestTime: Long) {
        while (available.isNotEmpty()) {
            val elapsedTime = requestTime - available.peek()
            require(elapsedTime >= 0) { "잘못된 요청 시간입니다." }
            if (elapsedTime.milliseconds < 1.seconds) {
                break
            }
            available.poll()
        }
    }
}
