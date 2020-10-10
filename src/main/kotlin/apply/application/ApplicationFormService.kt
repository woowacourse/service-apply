package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.Answer
import apply.domain.recruitmentitem.Answers
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
    private val applicantService: ApplicantService
) {
    fun findAllByRecruitmentId(recruitmentId: Long): List<ApplicationForm> =
        applicationFormRepository.findByRecruitmentId(recruitmentId)

    fun getByRecruitmentIdAndApplicantId(recruitmentId: Long, applicantId: Long): ApplicationForm =
        applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
            ?: throw IllegalArgumentException()

    private fun checkRecruitment(recruitmentId: Long) {
        val recruitment = recruitmentRepository.findByIdOrNull(recruitmentId)
        requireNotNull(recruitment) {
            "지원하는 모집이 존재하지 않습니다."
        }
        check(recruitment.isRecruiting) {
            "지원 불가능한 모집입니다."
        }
    }

    fun create(applicantId: Long, request: CreateApplicationFormRequest) {
        checkRecruitment(request.recruitmentId)
        require(!applicationFormRepository.existsByRecruitmentIdAndApplicantId(request.recruitmentId, applicantId)) {
            "작성중인 지원서가 존재합니다."
        }
        val applicationForm = ApplicationForm(applicantId, request.recruitmentId)
        applicationFormRepository.save(applicationForm)
    }

    fun update(applicantId: Long, request: UpdateApplicationFormRequest) {
        checkRecruitment(request.recruitmentId)

        val applicationForm: ApplicationForm =
            applicationFormRepository.findByRecruitmentIdAndApplicantId(
                request.recruitmentId,
                applicantId
            ) ?: throw IllegalArgumentException("작성중인 지원서가 존재하지 않습니다.")

        val answers = Answers(
            request.answers.map {
                Answer(
                    it.contents,
                    it.recruitmentItemId
                )
            }.toMutableList()
        )

        applicationForm.update(request.referenceUrl, answers)

        if (request.password.isNotBlank()) {
            applicantService.changePassword(applicantId, request.password)
        }

        if (request.isSubmitted) {
            applicationForm.submit()
        }
        applicationFormRepository.save(applicationForm)
    }

    fun findForm(applicantId: Long, recruitmentId: Long): ApplicationFormResponse {
        val form = applicationFormRepository.findByRecruitmentIdAndApplicantId(recruitmentId, applicantId)
            ?: throw IllegalArgumentException("해당하는 지원서가 없습니다.")
        val answers = form.answers.items.map { AnswerResponse(it.contents, it.recruitmentItemId) }
        return ApplicationFormResponse(
            id = form.id,
            recruitmentId = form.recruitmentId,
            referenceUrl = form.referenceUrl,
            submitted = form.submitted,
            answers = answers,
            createdDateTime = form.createdDateTime,
            modifiedDateTime = form.modifiedDateTime,
            submittedDateTime = form.submittedDateTime
        )
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
                submitted = false,
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
