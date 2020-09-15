package apply.domain.recruitmentitem

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
class Answer(
    val contents: String,

    @Column(nullable = false)
    val recruitmentItemId: Long
)
