package apply.domain.recruitment

import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

internal class RecruitmentPeriodTest : AnnotationSpec() {
    private lateinit var now: LocalDateTime

    @BeforeEach
    internal fun setUp() {
        now = LocalDateTime.now()
    }

    @Test
    fun `시작 일시와 종료 일시는 동일할 수 있다`() {
        shouldNotThrow<Exception> { RecruitmentPeriod(now, now) }
    }

    @Test
    fun `시작 일시는 종료 일시보다 이후일 수 없다`() {
        shouldThrowExactly<IllegalArgumentException> { RecruitmentPeriod(now, now.minusDays(1L)) }
    }

    @Test
    fun `시작 일시와 종료 일시가 같으면 같은 기간이다`() {
        RecruitmentPeriod(now, now) shouldBe RecruitmentPeriod(now, now)
    }

    @Test
    fun `모집 기간 전`() {
        val tomorrow = now.plusDays(1L)
        val period = RecruitmentPeriod(tomorrow, tomorrow)
        now.isBefore(period).shouldBeTrue()
    }

    @Test
    fun `모집 기간 중`() {
        val period = RecruitmentPeriod(now.minusDays(1L), now.plusDays(1L))
        now.isBetween(period).shouldBeTrue()
    }

    @Test
    fun `모집 기간 후`() {
        val yesterday = now.minusDays(1L)
        val period = RecruitmentPeriod(yesterday, yesterday)
        now.isAfter(period).shouldBeTrue()
    }
}
