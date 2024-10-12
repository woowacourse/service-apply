package apply.domain.mission

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class MissionPeriod(
    @Column(nullable = false)
    val startDateTime: LocalDateTime,

    @Column(nullable = false)
    val endDateTime: LocalDateTime,

    @Column(nullable = false)
    val submissionStartDateTime: LocalDateTime = startDateTime,
) {
    init {
        require(endDateTime >= startDateTime) { "시작 일시는 종료 일시보다 이후일 수 없습니다." }
        require(submissionStartDateTime in startDateTime..endDateTime) { "제출 시작 일시는 시작 일시와 종료 일시 사이여야 합니다." }
    }

    fun toSubmissionPeriod(): ClosedRange<LocalDateTime> = submissionStartDateTime..endDateTime
}
