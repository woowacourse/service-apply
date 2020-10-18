package apply.domain.applicant

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.TestConstructor
import support.createLocalDate

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@DataJpaTest
internal class ApplicantRepositoryTest(private val applicantRepository: ApplicantRepository) {
    @BeforeEach
    internal fun setUp() {
        val applicants = listOf(
            Applicant(
                "홍길동1",
                "a@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 4, 17),
                Password("password")
            ),
            Applicant(
                "홍길동2",
                "b@email.com",
                "010-0000-0000",
                Gender.FEMALE,
                createLocalDate(2020, 5, 5),
                Password("password")
            ),
            Applicant(
                "홍길동3",
                "c@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 1, 1),
                Password("password")
            )
        )
        applicantRepository.saveAll(applicants)
    }

    @ParameterizedTest
    @CsvSource("홍,3", "a@,1", "'',3", "4,0")
    fun `이름 또는 이메일에 검색 키워드가 포함되는 지원자들을 모두 조회한다`(keyword: String, expectedSize: Int) {
        val result = applicantRepository.findByInformationNameContainingOrInformationEmailContaining(keyword, keyword)
        assertThat(result).hasSize(expectedSize)
    }

    @Test
    fun `이메일이 일치하는 지원자를 조회한다`() {
        assertThat(applicantRepository.findByEmail("b@email.com")!!.name).isEqualTo("홍길동2")
    }

    @Test
    fun `이메일이 일치하는 지원자가 존재하지 않을 때, null을 반환한다`() {
        assertThat(applicantRepository.findByEmail("notexist@email.com")).isNull()
    }
}
