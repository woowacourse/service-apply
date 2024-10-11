package apply.domain.mission

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import java.time.LocalDateTime.now

class MissionPeriodTest : StringSpec({
    val now = now()

    "모든 일시는 같을 수 있다" {
        shouldNotThrowAny {
            MissionPeriod(startDateTime = now, submissionStartDateTime = now, endDateTime = now)
        }
    }

    "시작 일시는 종료 일시보다 이후일 수 없다" {
        shouldThrow<IllegalArgumentException> {
            MissionPeriod(startDateTime = now, endDateTime = now.minusDays(1L))
        }
    }

    "제출 시작 일시는 시작 일시와 종료 일시 사이여야 한다" {
        shouldThrow<IllegalArgumentException> {
            MissionPeriod(
                startDateTime = now,
                submissionStartDateTime = now.minusDays(1L),
                endDateTime = now
            )
        }
        shouldThrow<IllegalArgumentException> {
            MissionPeriod(
                startDateTime = now,
                submissionStartDateTime = now.plusDays(1L),
                endDateTime = now
            )
        }
    }
})
