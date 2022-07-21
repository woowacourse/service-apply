package apply.domain.mission

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import java.time.LocalDateTime.now

class MissionPeriodTest : StringSpec({
    val now = now()

    "시작 일시는 종료 일시보다 이후일 수 없다" {
        shouldThrow<IllegalArgumentException> { MissionPeriod(startDateTime = now, endDateTime = now.minusDays(1L)) }
    }
})
