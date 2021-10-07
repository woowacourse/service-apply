package apply.application

import apply.createApplicationForm
import apply.createUser
import apply.domain.applicationform.ApplicationFormRepository
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.user.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import support.test.UnitTest

@UnitTest
class ApplicantServiceTest {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var cheaterRepository: CheaterRepository

    @MockK
    private lateinit var applicationFormRepository: ApplicationFormRepository

    private lateinit var applicantService: ApplicantService

    @BeforeEach
    internal fun setUp() {
        applicantService = ApplicantService(applicationFormRepository, userRepository, cheaterRepository)
    }

    @Nested
    inner class Find {
        @BeforeEach
        internal fun setUp() {
            val userId = 1L
            val email = "email@email.com"

            slot<Long>().also { slot ->
                every { applicationFormRepository.findByRecruitmentIdAndSubmittedTrue(capture(slot)) } answers {
                    listOf(createApplicationForm(recruitmentId = slot.captured))
                }
            }

            every { cheaterRepository.findAll() } returns listOf(Cheater(email))
            slot<Iterable<Long>>().also { slot ->
                every { userRepository.findAllById(capture(slot)) } answers {
                    slot.captured.map { createUser(id = it, email = email) }
                }
            }

            slot<String>().also { slot ->
                every { userRepository.findAllByKeyword(capture(slot)) } answers {
                    listOf(createUser(name = slot.captured, id = userId, email = email))
                }
            }
        }

        @Test
        fun `지원자 정보와 부정 행위자 여부를 함께 제공한다`() {
            val actual = applicantService.findAllByRecruitmentIdAndKeyword(1L)

            assertThat(actual).hasSize(1)
            assertThat(actual[0].isCheater).isTrue
        }

        @Test
        fun `키워드로 찾은 지원자 정보와 부정 행위자 여부를 함께 제공한다`() {
            val actual = applicantService.findAllByRecruitmentIdAndKeyword(1L, "amazzi")

            assertThat(actual).hasSize(1)
            assertThat(actual[0].isCheater).isTrue
        }
    }
}
