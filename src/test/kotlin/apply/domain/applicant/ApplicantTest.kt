package apply.domain.applicant

import apply.PASSWORD
import apply.WRONG_PASSWORD
import apply.createApplicant
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class ApplicantTest {
    private lateinit var applicant: Applicant

    @BeforeEach
    internal fun setUp() {
        applicant = createApplicant()
    }

    @Test
    fun `지원자의 개인정보가 요청받은 개인정보와 일치하는지 확인한다`() {
        assertDoesNotThrow { applicant.authenticate(createApplicant()) }
    }

    @Test
    fun `지원자의 개인정보가 요청받은 개인정보와 다를 경우 예외가 발생한다`() {
        assertThatThrownBy { applicant.authenticate(createApplicant(name = "다른 이름")) }
            .isInstanceOf(ApplicantAuthenticationException::class.java)
    }

    @Test
    fun `지원자의 비밀번호와 일치하는지 확인한다`() {
        assertDoesNotThrow { applicant.authenticate(PASSWORD) }
    }

    @Test
    fun `지원자의 비밀번호와 다를 경우 예외가 발생한다`() {
        assertThatThrownBy { applicant.authenticate(WRONG_PASSWORD) }
            .isInstanceOf(ApplicantAuthenticationException::class.java)
    }
}
