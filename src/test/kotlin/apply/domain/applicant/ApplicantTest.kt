package apply.domain.applicant

import apply.createUser
import apply.domain.user.User
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class ApplicantTest {
    private lateinit var user: User

    @BeforeEach
    internal fun setUp() {
        user = createUser()
    }

    @Test
    fun `회원으로부터 지원자를 생성한다`() {
        val applicant = Applicant(user, 1L)

        assertThat(applicant.userId).isEqualTo(user.id)
        assertThat(applicant.information).isEqualTo(user.information)
    }
}
