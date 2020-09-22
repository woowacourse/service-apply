package apply.domain.answer

import apply.domain.applicationForm.ApplicationForm
import javax.persistence.*

@Entity
class Answer(
        @Column(nullable = false)
        val contents: String,

        @Column(nullable = false)
        val recruitmentItemId: Long
) {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L
}