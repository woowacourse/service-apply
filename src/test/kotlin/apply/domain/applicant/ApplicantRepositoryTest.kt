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
                "password"
            ),
            Applicant(
                "홍길동2",
                "b@email.com",
                "010-0000-0000",
                Gender.FEMALE,
                createLocalDate(2020, 5, 5),
                "password"
            ),
            Applicant(
                "홍길동3",
                "c@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 1, 1),
                "password"
            )
        )
        applicantRepository.saveAll(applicants)
    }

    @ParameterizedTest
    @CsvSource("홍,3", "a@,1", "'',3", "4,0")
    fun `findByNameContainingOrEmailContaining 메서드가가 올바르게 작동한다`(keyword: String, expectedSize: Int) {
        val result = applicantRepository.findByNameContainingOrEmailContaining(keyword, keyword)
        assertThat(result).hasSize(expectedSize)
    }

    @Test
    fun `findByEmail 메서드가 올바르게 작동한다`() {
        assertThat(applicantRepository.findByEmail("b@email.com")!!.name).isEqualTo("홍길동2")
    }

    @Test
    fun `존재하지 않는 이메일로 findByEmail 메서드를 사용할 경우 null을 반환한다`() {
        assertThat(applicantRepository.findByEmail("notexist@email.com")).isNull()
    }
}
