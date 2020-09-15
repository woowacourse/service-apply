package apply.domain.cheater

import apply.domain.applicant.Applicant
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class Cheater(
    @OneToOne
    @JoinColumn(name = "APPLICANT_ID")
    val applicant: Applicant,

    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0
)
