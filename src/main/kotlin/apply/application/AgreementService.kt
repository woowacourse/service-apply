package apply.application

import apply.domain.agreement.AgreementRepository
import apply.domain.agreement.getFirstByOrderByVersionDesc
import org.springframework.stereotype.Service

@Service
class AgreementService(
    private val agreementRepository: AgreementRepository,
) {
    fun latest(): AgreementResponse {
        return agreementRepository.getFirstByOrderByVersionDesc().let(::AgreementResponse)
    }
}
