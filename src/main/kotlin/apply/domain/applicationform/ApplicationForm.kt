package apply.domain.applicationForm

import apply.domain.recruitmentitem.Answers
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ApplicationForm(
        @Column(nullable = false)
        var applicantId: Long,

        @Column(nullable = false)
        var recruitmentId: Long,

        var referenceUrl: String,

        @Embedded
        var answers: Answers,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L
) {

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
            recruitmentId: Long,
            referenceUrl: String,
            answers: Answers,
            submitted: Boolean,
            createdDateTime: LocalDateTime,
            modifiedDateTime: LocalDateTime,
            submittedDateTime: LocalDateTime):this(applicantId, recruitmentId, referenceUrl, answers) {
        this.submitted = submitted
        this.createdDateTime = createdDateTime
        this.modifiedDateTime = modifiedDateTime
        this.submittedDateTime = submittedDateTime
    }
    fun update(referenceUrl: String, answers: Answers) {
        if (this.submitted) {
            throw IllegalAccessException("이미 제출된 지원서입니다. 수정할 수 없습니다.")
        }
        this.referenceUrl = referenceUrl
        this.modifiedDateTime = LocalDateTime.now()
        this.answers = answers
    }

    fun submit() {
        submitted = true
        submittedDateTime = LocalDateTime.now()
    }
}