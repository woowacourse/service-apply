package apply.domain.answer

import javax.persistence.*

@Entity
class Answer(
        @Column(nullable = false)
        val contents: String,

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0L
)