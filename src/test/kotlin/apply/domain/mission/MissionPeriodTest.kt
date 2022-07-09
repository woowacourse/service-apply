package apply.domain.mission

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import java.time.LocalDateTime

class MissionPeriodTest : AnnotationSpec() {
    private lateinit var now: LocalDateTime

    @BeforeEach
    internal fun setUp() {
        now = LocalDateTime.now()
    }

    @Test
    fun `시작 일시는 종료 일시보다 이후일 수 없다`() {
        shouldThrow<IllegalArgumentException> { MissionPeriod(now, now.minusDays(1L)) }
    }
}
