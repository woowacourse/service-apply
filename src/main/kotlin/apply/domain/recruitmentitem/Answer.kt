package apply.domain.recruitmentitem

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Lob

@Embeddable
class Answer(
    @Lob
    val contents: String,

    @Column(nullable = false)
    val recruitmentItemId: Long
)
