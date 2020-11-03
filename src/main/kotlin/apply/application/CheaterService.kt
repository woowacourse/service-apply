package apply.application

import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.toSort

@Transactional
@Service
class CheaterService(
    private val cheaterRepository: CheaterRepository
) {
    fun findAll(
        offset: Int,
        limit: Int,
        orders: Map<String, String>
    ): Page<CheaterResponse> {
        val page = offset / limit
        return cheaterRepository.findAllByPage(PageRequest.of(page, limit, orders.toSort()))
    }

    fun count() = cheaterRepository.count()
    //     cheaterRepository.findAll().map {
    //     val applicant = applicantRepository.findByIdOrNull(it.applicantId)!!
    //     CheaterResponse(it, applicant)
    // }

    fun save(applicantId: Long) {
        require(!cheaterRepository.existsByApplicantId(applicantId)) {
            "이미 등록된 부정 행위자입니다."
        }
        cheaterRepository.save(Cheater(applicantId))
    }

    fun deleteById(id: Long) {
        cheaterRepository.deleteById(id)
    }
}
