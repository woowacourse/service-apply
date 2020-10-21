package apply.domain.applicationform

import javax.persistence.CollectionTable
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.FetchType

@Embeddable
class ApplicationFormAnswers(
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "application_form_answers")
    val items: MutableList<ApplicationFormAnswer> = mutableListOf()
)
