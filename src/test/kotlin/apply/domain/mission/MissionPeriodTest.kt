package apply.domain.mission

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class MissionPeriodTest {
    private lateinit var now: LocalDateTime

    @BeforeEach
    internal fun setUp() {
        now = LocalDateTime.now()
    }

    @Test
    fun `시작 일시는 종료 일시보다 이후일 수 없다`() {
        assertThrows<IllegalArgumentException> {
            MissionPeriod(now, now.minusDays(1L))
        }
    }
}
