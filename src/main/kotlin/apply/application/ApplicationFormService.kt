package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
import apply.domain.recruitmentitem.RecruitmentItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import support.createLocalDateTime
import javax.annotation.PostConstruct
import javax.transaction.Transactional

@Transactional
@Service
class ApplicationFormService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository
) {

    fun findAllByRecruitmentId(recruitmentId: Long): List<ApplicationForm> =
        applicationFormRepository.findByRecruitmentId(recruitmentId)

    fun getByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): ApplicationForm =
        applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
            ?: throw IllegalArgumentException("해당하는 지원서가 없습니다.")

    fun getAllByApplicantId(applicantId: Long): List<MyApplicationFormResponse> =
        applicationFormRepository.findAllByApplicantId(applicantId).map(::MyApplicationFormResponse)

    fun create(applicantId: Long, request: CreateApplicationFormRequest) {
        checkRecruitment(request.recruitmentId)
        require(!applicationFormRepository.existsByRecruitmentIdAndApplicantId(request.recruitmentId, applicantId)) {
            "이미 지원한 이력이 있습니다."
        }
        val applicationForm = ApplicationForm(applicantId, request.recruitmentId)
        applicationFormRepository.save(applicationForm)
    }

    fun update(applicantId: Long, request: UpdateApplicationFormRequest) {
        checkRecruitment(request.recruitmentId)
        val applicationForm = getByRecruitmentIdAndApplicantId(request.recruitmentId, applicantId)
        val answers = Answers(
            request.answers.map {
                Answer(
                    it.contents,
                    it.recruitmentItemId
                )
            }.toMutableList()
        )
        if (request.submitted) {
            checkRecruitmentItem(request.recruitmentId, answers)
        }
        applicationForm.update(request.referenceUrl, answers)
        if (request.submitted) {
            require(!applicationFormRepository.existsByApplicantIdAndSubmittedTrue(applicantId)) {
                "이미 제출 완료한 지원서가 존재하여 제출할 수 없습니다."
            }
            applicationForm.submit()
        }
    }

    fun findForm(applicantId: Long, recruitmentId: Long): ApplicationFormResponse {
        val applicationForm = getByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
        return ApplicationFormResponse(applicationForm)
    }

    private fun checkRecruitment(recruitmentId: Long) {
        val recruitment = recruitmentRepository.findByIdOrNull(recruitmentId)
        requireNotNull(recruitment) {
            "지원하는 모집이 존재하지 않습니다."
        }
        check(recruitment.isRecruiting) {
            "지원 불가능한 모집입니다."
        }
    }

    private fun checkRecruitmentItem(recruitmentId: Long, answers: Answers) {
        val recruitmentItems = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(recruitmentId)
        require(answers.checkAllNotBlank() && (recruitmentItems.size == answers.items.size)) {
            "작성하지 않은 문항이 존재합니다."
        }
        require(
            recruitmentItems.all { recruitmentItem ->
                answers.items.find { recruitmentItem.id == it.recruitmentItemId }
                    .let { recruitmentItem.maximumLength >= it!!.contents.length }
            }
        ) {
            "유효하지 않은 문항이 존재합니다."
        }
    }

    @PostConstruct
    private fun populateDummy() {
        if (applicationFormRepository.count() != 0L) {
            return
        }
        val applicationForms = listOf(
            ApplicationForm(
                referenceUrl = "",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10, 10, 10),
                recruitmentId = 1L,
                applicantId = 1L,
                answers = Answers(
                    mutableListOf(
                        Answer("고객에게 가치를 전달하고 싶습니다.", 1L),
                        Answer("도전, 끈기", 2L)
                    )
                )
            ),
            ApplicationForm(
                referenceUrl = "https://www.google.com",
                submitted = true,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 5, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 5, 10, 10, 10),
                recruitmentId = 1L,
                applicantId = 2L,
                answers = Answers(
                    mutableListOf(
                        Answer("스타트업을 하고 싶습니다.", 1L),
                        Answer("책임감", 2L)
                    )
                )
            ),
            ApplicationForm(
                referenceUrl = "https://www.google.com",
                submitted = false,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 6, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 6, 10, 10, 10),
                recruitmentId = 1L,
                applicantId = 3L,
                answers = Answers(
                    mutableListOf(
                        Answer("바딘을 배우고 싶습니다.", 1L),
                        Answer("건강", 2L)
                    )
                )
            ),
            ApplicationForm(
                referenceUrl = "https://www.google.com",
                submitted = false,
                createdDateTime = createLocalDateTime(2019, 10, 25, 10),
                modifiedDateTime = createLocalDateTime(2019, 11, 6, 10),
                submittedDateTime = createLocalDateTime(2019, 11, 6, 10, 10, 10),
                recruitmentId = 1L,
                applicantId = 4L,
                answers = Answers(
                    mutableListOf(
                        Answer("코딩 교육을 하고 싶습니다.", 1L),
                        Answer("사랑", 2L)
                    )
                )
            )
        )
        applicationFormRepository.saveAll(applicationForms)
    }
}
