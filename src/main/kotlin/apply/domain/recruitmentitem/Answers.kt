package apply.domain.recruitmentitem

import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.FetchType

@Embeddable
class Answers(
    @ElementCollection(fetch = FetchType.EAGER)
    val items: MutableList<Answer> = mutableListOf()
)
