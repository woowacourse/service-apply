package apply.application

import apply.domain.applicationform.ApplicationForm
import apply.domain.applicationform.ApplicationFormAnswer
import apply.domain.applicationform.ApplicationFormAnswers
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.applicationform.UserValidator
import apply.domain.recruitment.Recruitment
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.recruitmentitem.RecruitmentItemRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Transactional
@Service
class ApplicationFormService(
    private val applicationFormRepository: ApplicationFormRepository,
    private val recruitmentRepository: RecruitmentRepository,
    private val recruitmentItemRepository: RecruitmentItemRepository,
    private val userValidator: UserValidator
) {
    fun create(userId: Long, request: CreateApplicationFormRequest) {
        val recruitment = findApplicableRecruitment(request.recruitmentId)
        check(!applicationFormRepository.existsByRecruitmentIdAndUserId(recruitment.id, userId)) {
            "이미 작성한 지원서가 있습니다."
        }
        applicationFormRepository.save(ApplicationForm(userId, recruitment.id, userValidator))
    }

    fun update(userId: Long, request: UpdateApplicationFormRequest) {
        val recruitment = findApplicableRecruitment(request.recruitmentId)
        validateRequest(request)
        val applicationForm = findByRecruitmentIdAndUserId(recruitment.id, userId)
        val answers = ApplicationFormAnswers(
            request.answers.map {
                ApplicationFormAnswer(it.contents, it.recruitmentItemId)
            }.toMutableList()
        )
        applicationForm.update(request.referenceUrl, answers)
        if (request.submitted) {
            applicationForm.submit(userValidator)
        }
    }

    fun getMyApplicationForms(userId: Long): List<MyApplicationFormResponse> =
        applicationFormRepository.findAllByUserId(userId).map(::MyApplicationFormResponse)

    fun getApplicationForm(userId: Long, recruitmentId: Long): ApplicationFormResponse {
        val applicationForm = findByRecruitmentIdAndUserId(recruitmentId, userId)
        check(!applicationForm.submitted) {
            "이미 제출한 지원서는 열람할 수 없습니다."
        }
        return ApplicationFormResponse(applicationForm)
    }

    private fun findByRecruitmentIdAndUserId(recruitmentId: Long, userId: Long): ApplicationForm =
        applicationFormRepository.findByRecruitmentIdAndUserId(recruitmentId, userId)
            ?: throw IllegalArgumentException("해당하는 지원서가 없습니다.")

    private fun findApplicableRecruitment(recruitmentId: Long): Recruitment {
        val recruitment = recruitmentRepository.findByIdOrNull(recruitmentId)
        requireNotNull(recruitment) {
            "지원하는 모집이 존재하지 않습니다."
        }
        check(recruitment.isRecruiting) {
            "지원 불가능한 모집입니다."
        }
        return recruitment
    }

    private fun validateRequest(request: UpdateApplicationFormRequest) {
        val recruitmentItems = recruitmentItemRepository.findByRecruitmentIdOrderByPosition(request.recruitmentId)
        if (request.submitted) {
            require(request.answers.all { it.contents.isNotBlank() } && (recruitmentItems.size == request.answers.size)) {
                "작성하지 않은 문항이 존재합니다."
            }
        }
        require(
            request.answers.all { answer ->
                recruitmentItems.first { answer.recruitmentItemId == it.id }.maximumLength >= answer.contents.length
            }
        ) {
            "모집 문항의 최대 글자 수를 초과하였습니다."
        }
    }
}
