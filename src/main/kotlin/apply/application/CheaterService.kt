package apply.application

import apply.domain.applicant.Applicant
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
    private val cheaters: MutableMap<Long, Cheater> = mutableMapOf()
    private val applicants: MutableMap<Long, Applicant> = mutableMapOf()

    init {
        cheaterRepository.findAll().forEach {
            val applicant = applicantRepository.findByIdOrNull(it.applicantId)!!
            cheaters[it.id] = it
            applicants[applicant.id] = applicant
        }
    }

    fun findAll(): List<CheaterResponse> =
        cheaters.map {
            val applicant = applicants.getValue(it.value.applicantId)
            CheaterResponse(it.key, applicant.name, applicant.email, it.value.createdDateTime)
        }

    fun save(applicantId: Long) {
        require(!applicants.containsKey(applicantId)) {
            "이미 등록된 부정 행위자입니다."
        }
        val cheater = cheaterRepository.save(Cheater(applicantId))
        cheaters[cheater.id] = cheater
        applicants[applicantId] = applicantRepository.findByIdOrNull(applicantId)!!
    }

    fun deleteById(id: Long) {
        cheaterRepository.deleteById(id)
        applicants.remove(cheaters.getValue(id).applicantId)
        cheaters.remove(id)
    }

    fun existsByApplicantId(applicantId: Long) = applicants.containsKey(applicantId)
}
