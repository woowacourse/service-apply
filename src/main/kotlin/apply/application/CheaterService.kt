package apply.application

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
    private val cheaters: MutableMap<Long, CheaterResponse> = mutableMapOf()

    init {
        cheaterRepository.findAll().forEach {
            val applicant = applicantRepository.findByIdOrNull(it.applicantId)!!
            cheaters[it.id] = CheaterResponse(it, applicant)
        }
    }

    fun findAll(): List<CheaterResponse> = cheaters.values.toList()

    fun save(applicantId: Long) {
        require(!containsApplicantId(applicantId)) {
            "이미 등록된 부정 행위자입니다."
        }
        val cheater = cheaterRepository.save(Cheater(applicantId))
        val applicant = applicantRepository.findByIdOrNull(applicantId)!!
        cheaters[cheater.id] = CheaterResponse(cheater, applicant)
    }

    fun deleteById(id: Long) {
        cheaterRepository.deleteById(id)
        cheaters.remove(id)
    }

    fun existsByApplicantId(applicantId: Long) = containsApplicantId(applicantId)

    private fun containsApplicantId(applicantId: Long) = cheaters.map { it.value.applicant.id }.contains(applicantId)
}
