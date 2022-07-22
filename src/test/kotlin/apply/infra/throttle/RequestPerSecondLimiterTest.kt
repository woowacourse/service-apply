package apply.infra.throttle

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class RequestPerSecondLimiterTest : StringSpec({
    "초당 1회로 제한할 경우 1초 간격 요청은 성공한다" {
        val limiter = RequestPerSecondLimiter(1)
        val currentTimeMillis = System.currentTimeMillis()
        assertSoftly(limiter) {
            tryAcquire(currentTimeMillis).shouldBeTrue()
            tryAcquire(currentTimeMillis + 1000).shouldBeTrue()
            tryAcquire(currentTimeMillis + 2000).shouldBeTrue()
        }
    }

    "초당 1회로 제한할 경우 초당 2번 요청하면 실패한다" {
        val limiter = RequestPerSecondLimiter(1)
        val currentTimeMillis = System.currentTimeMillis()
        assertSoftly(limiter) {
            tryAcquire(currentTimeMillis).shouldBeTrue()
            tryAcquire(currentTimeMillis + 500).shouldBeFalse()
            tryAcquire(currentTimeMillis + 1000).shouldBeTrue()
            tryAcquire(currentTimeMillis + 1500).shouldBeFalse()
        }
    }

    "초당 2회로 제한할 경우 초당 2회 요청은 성공한다" {
        val limiter = RequestPerSecondLimiter(2)
        val currentTimeMillis = System.currentTimeMillis()
        assertSoftly(limiter) {
            tryAcquire(currentTimeMillis).shouldBeTrue()
            tryAcquire(currentTimeMillis).shouldBeTrue()
            tryAcquire(currentTimeMillis + 1000).shouldBeTrue()
            tryAcquire(currentTimeMillis + 1500).shouldBeTrue()
            tryAcquire(currentTimeMillis + 2000).shouldBeTrue()
        }
    }

    "초당 2회로 제한할 경우 초당 3번 요청하면 실패한다" {
        val limiter = RequestPerSecondLimiter(2)
        val baseTime = System.currentTimeMillis()
        assertSoftly(limiter) {
            tryAcquire(baseTime).shouldBeTrue()
            tryAcquire(baseTime).shouldBeTrue()
            tryAcquire(baseTime).shouldBeFalse()
            tryAcquire(baseTime + 1000).shouldBeTrue()
            tryAcquire(baseTime + 1500).shouldBeTrue()
            tryAcquire(baseTime + 1600).shouldBeFalse()
            tryAcquire(baseTime + 2000).shouldBeTrue()
            tryAcquire(baseTime + 2100).shouldBeFalse()
        }
    }
})
