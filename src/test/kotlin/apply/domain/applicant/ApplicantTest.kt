package apply.domain.applicant

import apply.application.ApplicantInfo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import support.createLocalDate

internal class ApplicantTest {
    @Test
    fun `지원자의 각 필드가 request와 일치하는지 확인한다`() {
        val applicant = ApplicantInfo(
            "홍길동1",
            "a@email.com",
            "010-0000-0000",
            Gender.MALE,
            createLocalDate(2020, 4, 17),
            "password"
        )

        assertDoesNotThrow {
            applicant.toEntity().validate(applicant)
        }
    }
}
