package apply.domain.applicationform

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Lob

@Embeddable
class ApplicationFormAnswer(
    @Lob
    val contents: String,

    @Column(nullable = false)
    val recruitmentItemId: Long
)
