package apply.domain.applicationform

import support.domain.BaseRootEntity
import java.net.URL
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.UniqueConstraint

@Table(
    uniqueConstraints = [
        UniqueConstraint(name = "uk_application_form", columnNames = ["recruitmentId", "memberId"])
    ]
)
@Entity
class ApplicationForm(
    @Column(nullable = false)
    val memberId: Long,

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
) : BaseRootEntity<ApplicationForm>(id) {
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
        memberId: Long,
        recruitmentId: Long,
        applicationValidator: ApplicationValidator
    ) : this(memberId, recruitmentId) {
        applicationValidator.validate(memberId, recruitmentId)
    }

    fun update(referenceUrl: String, applicationFormAnswers: ApplicationFormAnswers) {
        checkSubmitted()
        if (referenceUrl.isNotEmpty()) {
            validateUrl(referenceUrl)
        }
        this.referenceUrl = referenceUrl
        modifiedDateTime = LocalDateTime.now()
        answers = applicationFormAnswers
    }

    private fun validateUrl(url: String) {
        try {
            URL(url)
        } catch (e: Exception) {
            throw IllegalArgumentException("올바른 형식의 URL이 아닙니다.")
        }
    }

    fun submit(applicationValidator: ApplicationValidator) {
        checkSubmitted()
        applicationValidator.validate(memberId, recruitmentId)
        submitted = true
        submittedDateTime = LocalDateTime.now()
        registerEvent(ApplicationFormSubmittedEvent(id, memberId, recruitmentId))
    }

    private fun checkSubmitted() {
        check(!submitted) { "이미 제출된 지원서입니다. 수정할 수 없습니다." }
    }
}
