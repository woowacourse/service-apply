package support.infra

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec

class RateLimiterTest : StringSpec({
    "초당 1회로 제한할 경우 1초 간격 요청은 성공한다" {
        val limiter = RateLimiter(1)
        val current = System.currentTimeMillis()
        assertSoftly(limiter) {
            shouldNotThrowAny { acquire(current) }
            shouldNotThrowAny { acquire(current + 1000) }
            shouldNotThrowAny { acquire(current + 2000) }
        }
    }

    "초당 1회로 제한할 경우 초당 2번 요청하면 실패한다" {
        val limiter = RateLimiter(1)
        val current = System.currentTimeMillis()
        assertSoftly(limiter) {
            shouldNotThrowAny { acquire(current) }
            shouldThrow<ExceededRequestException> { acquire(current + 500) }
            shouldNotThrowAny { acquire(current + 1000) }
            shouldThrow<ExceededRequestException> { acquire(current + 1500) }
        }
    }

    "초당 2회로 제한할 경우 초당 2회 요청은 성공한다" {
        val limiter = RateLimiter(2)
        val current = System.currentTimeMillis()
        assertSoftly(limiter) {
            shouldNotThrowAny { acquire(current) }
            shouldNotThrowAny { acquire(current) }
            shouldNotThrowAny { acquire(current + 1000) }
            shouldNotThrowAny { acquire(current + 1500) }
            shouldNotThrowAny { acquire(current + 2000) }
        }
    }

    "초당 2회로 제한할 경우 초당 3번 요청하면 실패한다" {
        val limiter = RateLimiter(2)
        val current = System.currentTimeMillis()
        assertSoftly(limiter) {
            shouldNotThrowAny { acquire(current) }
            shouldNotThrowAny { acquire(current) }
            shouldThrow<ExceededRequestException> { acquire(current) }
            shouldNotThrowAny { acquire(current + 1000) }
            shouldNotThrowAny { acquire(current + 1500) }
            shouldThrow<ExceededRequestException> { acquire(current + 1600) }
            shouldNotThrowAny { acquire(current + 2000) }
            shouldThrow<ExceededRequestException> { acquire(current + 2100) }
        }
    }
})
