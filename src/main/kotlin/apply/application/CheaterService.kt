package apply.application

import apply.domain.applicant.Applicant
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class CheaterService(private val cheaterRepository: CheaterRepository) {
    fun findAll(): List<Cheater> = cheaterRepository.findAll()

    fun save(applicant: Applicant) {
        if (cheaterRepository.existsByApplicant(applicant)) {
            return
        }
        cheaterRepository.save(Cheater(applicant))
    }

    fun delete(cheater: Cheater) = cheaterRepository.delete(cheater)
}
