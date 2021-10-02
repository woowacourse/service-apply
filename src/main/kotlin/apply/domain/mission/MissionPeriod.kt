package apply.domain.mission

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class MissionPeriod(
    @Column(nullable = false)
    val startDateTime: LocalDateTime,

    @Column(nullable = false)
    val endDateTime: LocalDateTime
) {
    init {
        require(endDateTime >= startDateTime) { "시작 일시는 종료 일시보다 이후일 수 없습니다." }
    }
}
