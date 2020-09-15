package apply.application

import apply.domain.application.Application
import apply.domain.application.ApplicationRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import support.createLocalDateTime
import javax.annotation.PostConstruct

@Transactional
@Service
class ApplicationService(
    private val applicationRepository: ApplicationRepository
) {
    fun getByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): Application =
        applicationRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
            ?: throw IllegalArgumentException()

    @PostConstruct
    private fun populateDummy() {
        if (applicationRepository.count() != 0L) {
            return
        }
        val applications = listOf(
            Application(
                "",
                true,
                createLocalDateTime(2019, 10, 25, 10),
                createLocalDateTime(2019, 11, 5, 10),
                createLocalDateTime(2019, 11, 5, 10),
                1L,
                1L,
                Answers(
                    mutableListOf(
                        Answer("고객에게 가치를 전달하고 싶습니다.", 1L),
                        Answer("도전, 끈기", 2L)
                    )
                )
            ),
            Application(
                "",
                true,
                createLocalDateTime(2019, 10, 25, 10),
                createLocalDateTime(2019, 11, 5, 10),
                createLocalDateTime(2019, 11, 5, 10),
                1L,
                2L,
                Answers(
                    mutableListOf(
                        Answer("스타트업을 하고 싶습니다.", 1L),
                        Answer("책임감", 2L)
                    )
                )
            )
        )
        applicationRepository.saveAll(applications)
    }
}
