package apply.domain.applicationForm

import apply.domain.answer.Answer
import apply.domain.applicant.Applicant
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class ApplicationForm(
        @Column(nullable = false)
        val applicantId: Long,

        @Column
        var referenceUrl: String,

        @Column(nullable = false)
        var submitted: Boolean = false,

        @Column(nullable = false)
        val createdDateTime: LocalDateTime = LocalDateTime.now(),

        @Column(nullable = false)
        var modifiedDateTime: LocalDateTime = LocalDateTime.now(),

        @Column
        var submittedDateTime: LocalDateTime,

        @OneToMany
        @JoinColumn(name = "FORM_ID")
        val answers: List<Answer> = ArrayList(),

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L
) {
    fun update(referenceUrl: String) {
        this.referenceUrl = referenceUrl
        this.modifiedDateTime = LocalDateTime.now()

    }

    fun submit() {
        submitted = true
        submittedDateTime = LocalDateTime.now()
    }
}