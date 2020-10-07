package apply.domain.applicant

import apply.application.ApplicantInformation
import apply.domain.applicant.exception.ApplicantValidateException
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import support.createLocalDate

internal class ApplicantTest {
    private lateinit var applicantInformation: ApplicantInformation
    private lateinit var applicant: Applicant

    @BeforeEach
    internal fun setUp() {
        applicantInformation = ApplicantInformation(
            "홍길동1",
            "a@email.com",
            "010-0000-0000",
            Gender.MALE,
            createLocalDate(2020, 4, 17),
            "password"
        )
        applicant = applicantInformation.toEntity()
    }

    @Test
    fun `지원자의 개인정보가 요청받은 개인정보와 일치하는지 확인한다`() {
        assertDoesNotThrow { applicant.validate(applicantInformation) }
    }

    @Test
    fun `지원자의 개인정보가 요청받은 개인정보와 다를 경우 예외가 발생한다`() {
        val invalidApplicantInformation = applicantInformation.copy(name = "다른 이름")

        assertThatThrownBy { applicant.validate(invalidApplicantInformation) }
            .isInstanceOf(ApplicantValidateException::class.java)
            .hasMessage("요청 정보가 기존 지원자 정보와 일치하지 않습니다")
    }
}
