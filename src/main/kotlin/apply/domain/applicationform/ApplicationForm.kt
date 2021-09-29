package apply.domain.applicationform

import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "uk_application_form", columnNames = ["recruitmentId", "userId"])
    ]
)
@Entity
class ApplicationForm(
    @Column(nullable = false)
    val userId: Long,

    @Column(nullable = false)
    val recruitmentId: Long,
    referenceUrl: String = "",
    answers: ApplicationFormAnswers = ApplicationFormAnswers(),
    submitted: Boolean = false,
    submittedDateTime: LocalDateTime? = null,

    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now(),
    modifiedDateTime: LocalDateTime = LocalDateTime.now(),
    id: Long = 0L
) : BaseEntity(id) {
    var referenceUrl: String = referenceUrl
        private set

    @Embedded
    var answers: ApplicationFormAnswers = answers
        private set

    @Column(nullable = false)
    var submitted: Boolean = submitted
        private set

    @Column
    var submittedDateTime: LocalDateTime? = submittedDateTime
        private set

    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime = modifiedDateTime
        private set

    init {
        if (submitted) {
            requireNotNull(submittedDateTime)
        }
    }

    constructor(
        userId: Long,
        recruitmentId: Long,
        userValidator: UserValidator
    ) : this(userId, recruitmentId) {
        userValidator.validate(userId, recruitmentId)
    }

    fun update(referenceUrl: String, applicationFormAnswers: ApplicationFormAnswers) {
        check(!this.submitted) {
            "이미 제출된 지원서입니다. 수정할 수 없습니다."
        }
        this.referenceUrl = referenceUrl
        this.modifiedDateTime = LocalDateTime.now()
        this.answers = applicationFormAnswers
    }

    fun submit(userValidator: UserValidator) {
        userValidator.validate(userId, recruitmentId)
        submitted = true
        submittedDateTime = LocalDateTime.now()
    }
}
