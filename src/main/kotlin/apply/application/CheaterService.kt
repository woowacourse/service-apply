package apply.application

import apply.domain.applicant.Applicant
import apply.domain.applicant.ApplicantRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CheaterService(
    private val applicantRepository: ApplicantRepository,
    private val cheaterRepository: CheaterRepository
) {
    fun findAll(): List<Pair<Cheater, Applicant>> =
        cheaterRepository.findAll().map { it to applicantRepository.findByIdOrNull(it.applicantId)!! }

    fun save(applicantId: Long) {
        if (cheaterRepository.existsByApplicantId(applicantId)) {
            throw IllegalArgumentException("이미 등록된 부정 행위자입니다.")
        }
        cheaterRepository.save(Cheater(applicantId))
    }

    fun deleteById(id: Long) = cheaterRepository.deleteById(id)
}
