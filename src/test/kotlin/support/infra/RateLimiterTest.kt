package support.infra

import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import kotlin.time.Duration.Companion.milliseconds

private val Int.ms: Long get() = milliseconds.inWholeMilliseconds

class RateLimiterTest : StringSpec({
    "초당 1회로 제한할 경우 1초 간격 요청은 성공한다" {
        val limiter = RateLimiter(1)
        listOf(0.ms, 1000.ms, 2000.ms).forAll {
            shouldNotThrowAny { limiter.acquire(it) }
        }
    }

    "초당 1회로 제한할 경우 초당 2번 요청하면 실패한다" {
        val limiter = RateLimiter(1).apply {
            acquire(0.ms)
        }
        listOf(100.ms, 500.ms, 900.ms).forAll {
            shouldThrow<ExceededRequestException> { limiter.acquire(it) }
        }
    }

    "초당 2회로 제한할 경우 초당 2회 요청은 성공한다" {
        val limiter = RateLimiter(2)
        listOf(0.ms, 500.ms, 1000.ms, 1500.ms, 2000.ms).forAll {
            shouldNotThrowAny { limiter.acquire(it) }
        }
    }

    "초당 2회로 제한할 경우 초당 3번 요청하면 실패한다" {
        val limiter = RateLimiter(2).apply {
            acquire(500.ms)
            acquire(600.ms)
        }
        assertSoftly(limiter) {
            shouldThrow<ExceededRequestException> { acquire(1400.ms) }
            shouldNotThrowAny { acquire(1500.ms) }
        }
    }

    "요청은 순서대로 이루어져야 한다" {
        val limiter = RateLimiter(2).apply {
            acquire(1000.ms)
        }
        shouldThrow<IllegalArgumentException> { limiter.acquire(0.ms) }
    }

    "경쟁 상태에서도 순서를 유지한다" {
        runTest {
            val limiter = RateLimiter(1000)
            launch {
                repeat(1000) {
                    shouldNotThrowAny { limiter.acquire() }
                }
            }.join()
        }
    }
})
