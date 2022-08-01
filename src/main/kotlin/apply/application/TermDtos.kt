package apply.application

import apply.domain.term.Term
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class TermData(
    @field:NotBlank
    @field:Size(min = 1, max = 31)
    var name: String = "",
    var id: Long = 0L
) {
    constructor(term: Term) : this(term.name, term.id)
}

data class TermResponse(
    val id: Long,
    val name: String
) {
    constructor(term: Term) : this(term.id, term.name)
}
