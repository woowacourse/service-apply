package apply.domain.applicant

import apply.createApplicant
import apply.createUser
import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.UserRepository
import apply.domain.user.findByEmail
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import support.createLocalDate
import support.test.RepositoryTest

@RepositoryTest
internal class ApplicantRepositoryTest(
    private val applicantRepository: ApplicantRepository,
    private val userRepository: UserRepository
) {
    @BeforeEach
    internal fun setUp() {
        val users = listOf(
            createUser(
                "홍길동1",
                "a@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 4, 17),
                Password("password")
            ),
            createUser(
                "홍길동2",
                "b@email.com",
                "010-0000-0000",
                Gender.FEMALE,
                createLocalDate(2020, 5, 5),
                Password("password")
            ),
            createUser(
                "홍길동3",
                "c@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 1, 1),
                Password("password")
            )
        )
        userRepository.saveAll(users)
            .map { createApplicant(it) }
            .let { applicantRepository.saveAll(it) }
    }

    @ParameterizedTest
    @CsvSource("홍,3", "a@,1", "'',3", "4,0")
    fun `이름 또는 이메일에 검색 키워드가 포함되는 지원자들을 모두 조회한다`(keyword: String, expectedSize: Int) {
        val result = applicantRepository.findAllByKeyword(keyword)
        assertThat(result).hasSize(expectedSize)
    }

    @Test
    fun `이메일이 일치하는 지원자를 조회한다`() {
        assertThat(applicantRepository.findByEmailAndRecruitmentId("b@email.com", 0L)!!.name).isEqualTo("홍길동2")
    }

    @Test
    fun `이메일이 일치하는 지원자가 존재하지 않을 때, null을 반환한다`() {
        assertThat(applicantRepository.findByEmailAndRecruitmentId("notexist@email.com", 0L)).isNull()
    }

    @Test
    fun `이메일이 일치하는 지원자들을 전부 조회한다`() {
        val email = "a@email.com"
        applicantRepository.save(
            createApplicant(userRepository.findByEmail(email)!!)
        )

        assertThat(applicantRepository.findAllByEmail(email)).hasSize(2)
    }

    @Test
    fun `이메일들이 일치하는 지원자들을 전부 조회한다`() {
        val emails = listOf("b@email.com", "c@email.com")
        assertThat(applicantRepository.findAllByEmailIn(emails)).hasSize(2)
    }

    @Test
    fun `이메일이 일치하는 지원자 있는지 확인한다`() {
        assertAll(
            { assertThat(applicantRepository.existsByEmail("a@email.com")).isTrue() },
            { assertThat(applicantRepository.existsByEmail("non-exists@email.com")).isFalse() }
        )
    }
}
