package apply.domain.recruitment

import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Recruitment(
    @Column(nullable = false)
    var title: String,

    @Embedded
    var period: RecruitmentPeriod,
    canRecruit: Boolean = false,
    isHidden: Boolean = true,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
) {
    @Column(nullable = false)
    var canRecruit: Boolean = canRecruit
        private set

    @Column(nullable = false)
    var isHidden: Boolean = isHidden
        private set

    val startDateTime: LocalDateTime
        get() = period.startDateTime

    val endDateTime: LocalDateTime
        get() = period.endDateTime

    val status: RecruitmentStatus
        get() = RecruitmentStatus.of(period, canRecruit)

    val isEnded: Boolean
        get() = status == RecruitmentStatus.ENDED

    constructor(
        title: String,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        canRecruit: Boolean = false,
        isHidden: Boolean = true,
        id: Long = 0L
    ) : this(title, RecruitmentPeriod(startDateTime, endDateTime), canRecruit, isHidden, id)
}
