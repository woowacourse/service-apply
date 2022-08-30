package apply.application.tobe

import apply.application.ApplicationFormService
import apply.application.CreateApplicationFormRequest
import apply.application.UpdateApplicationFormRequest
import apply.createApplicationForm
import apply.createRecruitment
import apply.createUser
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.applicationform.DuplicateApplicationException
import apply.domain.recruitment.RecruitmentRepository
import apply.domain.user.UserRepository
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.longs.shouldNotBeZero
import support.test.IntegrationTest
import support.test.spec.afterRootTest
import java.time.LocalDateTime.now

@IntegrationTest
class ApplicationFormIntegrationTest(
    private val applicationFormService: ApplicationFormService,
    private val userRepository: UserRepository,
    private val applicationFormRepository: ApplicationFormRepository,
    private val recruitmentRepository: RecruitmentRepository
) : BehaviorSpec({
    Given("지원하지 않은 모집 중 모집이 있는 경우") {
        val recruitment = recruitmentRepository.save(createRecruitment(recruitable = true))
        val user = userRepository.save(createUser())

        When("특정 회원이 해당 모집에 지원서를 생성하면") {
            val actual = applicationFormService.create(user.id, CreateApplicationFormRequest(recruitment.id))

            Then("임시 저장된 지원서가 생성된다") {
                actual.id.shouldNotBeZero()
                actual.submitted.shouldBeFalse()
            }
        }
    }

    Given("이미 최종 지원서를 제출한 회원과 모집 중 모집이 있는 경우") {
        val recruitment = recruitmentRepository.save(createRecruitment(recruitable = true))
        val user = userRepository.save(createUser())
        applicationFormRepository.save(
            createApplicationForm(user.id, recruitment.id, submitted = true, submittedDateTime = now())
        )

        When("동일한 모집에 대해 중복으로 지원서를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<IllegalStateException> {
                    applicationFormService.create(user.id, CreateApplicationFormRequest(recruitment.id))
                }
            }
        }
    }

    Given("동일한 기수의 특정 모집에 이미 최종 지원서를 제출한 회원이 있는 경우") {
        val termId = 1L
        val recruitment1 = recruitmentRepository.save(createRecruitment(termId = termId))
        val recruitment2 = recruitmentRepository.save(createRecruitment(termId = termId, recruitable = true))
        val user = userRepository.save(createUser())
        applicationFormRepository.save(
            createApplicationForm(user.id, recruitment1.id, submitted = true, submittedDateTime = now())
        )

        When("동일한 기수의 다른 모집에 지원서를 생성하면") {
            Then("예외가 발생한다") {
                shouldThrow<DuplicateApplicationException> {
                    applicationFormService.create(user.id, CreateApplicationFormRequest(recruitment2.id))
                }
            }
        }
    }

    Given("동일 기수의 복수 모집에 지원서를 임시 저장한 회원이 있는 경우") {
        val termId = 1L
        val recruitment1 = recruitmentRepository.save(createRecruitment(termId = termId))
        val recruitment2 = recruitmentRepository.save(createRecruitment(termId = termId, recruitable = true))
        val user = userRepository.save(createUser())
        applicationFormRepository.save(createApplicationForm(user.id, recruitment1.id, submitted = false))
        applicationFormRepository.save(createApplicationForm(user.id, recruitment2.id, submitted = false))

        When("동일 기수의 다른 모집에 지원서를 최종 제출하면") {
            Then("최종 지원서를 제출할 수 있다") {
                shouldNotThrowAny {
                    applicationFormService.update(
                        user.id,
                        UpdateApplicationFormRequest(recruitmentId = recruitment2.id, submitted = true)
                    )
                }
            }
        }
    }

    Given("동일 기수의 특정 모집에 지원서를 최종 제출한 회원이 있는 경우") {
        val termId = 1L
        val recruitment1 = recruitmentRepository.save(createRecruitment(termId = termId))
        val recruitment2 = recruitmentRepository.save(createRecruitment(termId = termId, recruitable = true))
        val user = userRepository.save(createUser())
        applicationFormRepository.save(
            createApplicationForm(user.id, recruitment1.id, submitted = true, submittedDateTime = now())
        )
        applicationFormRepository.save(createApplicationForm(user.id, recruitment2.id, submitted = false))

        When("동일 기수의 다른 모집에 지원서를 최종 제출하면") {
            Then("예외가 발생한다") {
                shouldThrow<DuplicateApplicationException> {
                    applicationFormService.update(
                        user.id,
                        UpdateApplicationFormRequest(recruitmentId = recruitment2.id, submitted = true)
                    )
                }
            }
        }
    }

    afterRootTest {
        userRepository.deleteAll()
        applicationFormRepository.deleteAll()
        recruitmentRepository.deleteAll()
    }
})
