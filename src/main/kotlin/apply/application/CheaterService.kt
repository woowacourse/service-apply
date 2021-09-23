package apply.application

import apply.domain.applicant.ApplicantRepository
import apply.domain.applicant.findByEmail
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CheaterService(
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun findAll(): List<CheaterResponse> = cheaterRepository.findAll().map {
        val applicant = applicantRepository.findByEmail(it.email)
        CheaterResponse(it, applicant)
    }

    fun save(request: CheaterData) {
        val email = request.email
        require(!cheaterRepository.existsByEmail(email)) {
            "이미 등록된 부정 행위자입니다."
        }
        cheaterRepository.save(Cheater(email, request.description))
    }

    fun deleteById(id: Long) {
        cheaterRepository.deleteById(id)
    }
}
