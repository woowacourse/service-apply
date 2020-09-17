package apply.domain.application

import apply.domain.recruitmentitem.Answers
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Application(
    val referenceUrl: String,

    @Column(nullable = false)
    val submitted: Boolean,

    @Column(nullable = false)
    val createdDateTime: LocalDateTime,

    @Column(nullable = false)
    val modifiedDateTime: LocalDateTime,

    @Column(nullable = false)
    val submittedDateTime: LocalDateTime,

    @Column(nullable = false)
    val recruitmentId: Long,

    @Column(nullable = false)
    val applicantId: Long,

    @Embedded
    val answers: Answers,

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L
)
