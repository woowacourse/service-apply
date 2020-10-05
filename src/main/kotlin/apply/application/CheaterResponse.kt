package apply.application

import apply.domain.applicant.Applicant
import apply.domain.cheater.Cheater
import java.time.LocalDateTime

data class CheaterResponse(
    val id: Long,
    val createdDateTime: LocalDateTime,
    val applicant: Applicant
) {
    constructor(cheater: Cheater, applicant: Applicant) : this(
        cheater.id,
        cheater.createdDateTime,
        applicant
    )
}
