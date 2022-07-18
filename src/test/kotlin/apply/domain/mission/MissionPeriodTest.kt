package apply.domain.mission

import io.kotest.core.spec.style.StringSpec
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime

class MissionPeriodTest : StringSpec({
    lateinit var now: LocalDateTime

    beforeEach { now = LocalDateTime.now() }

    "시작 일시는 종료 일시보다 이후일 수 없다" {
        assertThrows<IllegalArgumentException> {
            MissionPeriod(now, now.minusDays(1L))
        }
    }
})
