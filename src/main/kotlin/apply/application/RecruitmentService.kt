package apply.application

import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDateTime
import javax.annotation.PostConstruct

@Transactional
@Service
class RecruitmentService(private val recruitmentRepository: RecruitmentRepository) {
    fun findAll(): List<Recruitment> {
        return recruitmentRepository.findAll()
    }

    fun deleteById(id: Long) {
        recruitmentRepository.deleteById(id)
    }

    fun start(id: Long) {
        getById(id).start()
    }

    fun stop(id: Long) {
        getById(id).stop()
    }

    fun getById(id: Long): Recruitment =
        recruitmentRepository.findByIdOrNull(id) ?: throw IllegalArgumentException()

    @PostConstruct
    private fun populateDummy() {
        if (recruitmentRepository.count() != 0L) {
            return
        }
        val recruitments = listOf(
            Recruitment(
                title = "웹 백엔드 3기",
                canRecruit = true,
                startDateTime = createLocalDateTime(2020, 10, 25, 15),
                endDateTime = createLocalDateTime(2020, 11, 5, 10)
            ),
            Recruitment(
                title = "웹 프론트엔드 3기",
                canRecruit = false,
                startDateTime = createLocalDateTime(2020, 10, 25, 15),
                endDateTime = createLocalDateTime(2020, 11, 5, 10)
            )
        )
        recruitmentRepository.saveAll(recruitments)
    }
}
