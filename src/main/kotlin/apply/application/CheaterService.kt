package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.cheater.CheaterResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CheaterService(
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun findAll(): List<CheaterResponse> =
        cheaterRepository.findAll().map {
            val applicant = applicantRepository.findByIdOrNull(it.applicantId)!!
            CheaterResponse(it.id, applicant.name, applicant.email, it.createdDateTime)
        }

    fun save(applicantId: Long) {
        require(!cheaterRepository.existsByApplicantId(applicantId)) {
            "이미 등록된 부정 행위자입니다."
        }
        cheaterRepository.save(Cheater(applicantId))
    }

    fun deleteById(id: Long) = cheaterRepository.deleteById(id)
}
