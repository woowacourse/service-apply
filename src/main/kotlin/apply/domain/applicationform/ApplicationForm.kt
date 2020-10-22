package apply.domain.applicationform

import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity

@Entity
class ApplicationForm(
    @Column(nullable = false)
    val applicantId: Long,

    @Column(nullable = false)
    val recruitmentId: Long,

    var referenceUrl: String,

    @Embedded
    var answers: ApplicationFormAnswers,
    id: Long = 0L
) : BaseEntity(id) {
    @Column(nullable = false)
    var submitted: Boolean = false

    @Column(nullable = false)
    var createdDateTime: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()

    @Column
    var submittedDateTime: LocalDateTime? = null

    constructor(
        applicantId: Long,
        recruitmentId: Long
    ) : this(applicantId, recruitmentId, "", ApplicationFormAnswers())

    constructor(
        applicantId: Long,
        recruitmentId: Long,
        referenceUrl: String,
        applicationFormAnswers: ApplicationFormAnswers,
        submitted: Boolean,
        createdDateTime: LocalDateTime,
        modifiedDateTime: LocalDateTime,
        submittedDateTime: LocalDateTime?
    ) : this(applicantId, recruitmentId, referenceUrl, applicationFormAnswers) {
        this.submitted = submitted
        this.createdDateTime = createdDateTime
        this.modifiedDateTime = modifiedDateTime
        this.submittedDateTime = submittedDateTime
    }

    constructor(
        applicantId: Long,
        recruitmentId: Long,
        referenceUrl: String,
        submitted: Boolean,
        createdDateTime: LocalDateTime,
        modifiedDateTime: LocalDateTime,
        submittedDateTime: LocalDateTime,
        applicationFormAnswers: ApplicationFormAnswers,
        id: Long
    ) : this(applicantId, recruitmentId, referenceUrl, applicationFormAnswers, id) {
        this.submitted = submitted
        this.createdDateTime = createdDateTime
        this.modifiedDateTime = modifiedDateTime
        this.submittedDateTime = submittedDateTime
    }

    fun update(referenceUrl: String, applicationFormAnswers: ApplicationFormAnswers) {
        check(!this.submitted) {
            "이미 제출된 지원서입니다. 수정할 수 없습니다."
        }
        this.referenceUrl = referenceUrl
        this.modifiedDateTime = LocalDateTime.now()
        this.answers = applicationFormAnswers
    }

    fun submit(submittedDateTime: LocalDateTime) {
        this.submitted = true
        this.submittedDateTime = submittedDateTime
    }

    fun submit() {
        submit(LocalDateTime.now())
    }
}
