package apply.infra.throttle

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class RequestPerSecondLimiterTest {

    @Test
    fun `초당 1회로 제한할 경우 1초 간격 요청은 성공한다`() {
        val requestPerSecondLimiter = RequestPerSecondLimiter(1)
        val baseTime = System.currentTimeMillis()

        assertAll(
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 1000)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 2000)).isTrue }
        )
    }

    @Test
    fun `초당 1회로 제한할 경우 초당 2번 요청하면 실패한다`() {
        val requestPerSecondLimiter = RequestPerSecondLimiter(1)
        val baseTime = System.currentTimeMillis()

        assertAll(
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 999)).isFalse },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 1000)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 1999)).isFalse }
        )
    }

    @Test
    fun `초당 2회로 제한할 경우 초당 2회 요청은 성공한다`() {
        val requestPerSecondLimiter = RequestPerSecondLimiter(2)
        val baseTime = System.currentTimeMillis()

        assertAll(
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 1001)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 1500)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 2001)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 4000)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 4000)).isTrue }
        )
    }

    @Test
    fun `초당 2회로 제한할 경우 초당 3번 요청하면 실패한다`() {
        val requestPerSecondLimiter = RequestPerSecondLimiter(2)
        val baseTime = System.currentTimeMillis()

        assertAll(
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime)).isFalse },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 1000)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 1500)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 1600)).isFalse },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 2000)).isTrue },
            { assertThat(requestPerSecondLimiter.tryAcquire(baseTime + 2400)).isFalse }
        )
    }
}
