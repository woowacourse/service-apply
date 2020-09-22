package apply.domain.applicationForm

import apply.domain.answer.Answer
import apply.domain.applicant.Applicant
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ApplicationForm(
        @Column(nullable = false)
        val applicantId: Long,

        @Column(nullable = false)
        val recruitmentId: Long,

        @Column
        var referenceUrl: String,

        @OneToMany
        @JoinColumn(name = "FORM_ID")
        var answers: List<Answer> = ArrayList(),

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L
) {
    @Column(nullable = false)
    var submitted: Boolean = false

    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now()

    @Column(nullable = false)
    var modifiedDateTime: LocalDateTime = LocalDateTime.now()

    @Column
    var submittedDateTime: LocalDateTime? = null

    fun update(referenceUrl: String, answers: List<Answer>) {
        if(this.submitted == true) {
            throw IllegalAccessException("이미 제출된 지원서입니다. 수정할 수 없습니다.")
        }
        this.referenceUrl = referenceUrl
        this.answers = answers
        this.modifiedDateTime = LocalDateTime.now()
    }

    fun submit() {
        submitted = true
        submittedDateTime = LocalDateTime.now()
    }
}