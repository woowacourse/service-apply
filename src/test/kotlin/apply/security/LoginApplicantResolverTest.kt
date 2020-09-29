package apply.security

import apply.application.ApplicantService
import apply.domain.applicant.Applicant
import apply.domain.applicant.Gender
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import support.createLocalDate

@ExtendWith(MockKExtension::class)
internal class LoginApplicantResolverTest {

    @MockK
    private lateinit var applicantService: ApplicantService

    private lateinit var loginApplicantResolver: LoginApplicantResolver

    private lateinit var loginApplicantParameter: MethodParameter

    @BeforeEach
    internal fun setUp() {
        loginApplicantResolver = LoginApplicantResolver(applicantService)

        val method = TestAuthController::class.java.getDeclaredMethod("authTargetMethod", Applicant::class.java)
        loginApplicantParameter = MethodParameter.forExecutable(method, 0)
    }

    @Test
    fun `@LoginApplicant 어노테이션을 서포트하는지 확인한다`() {
        assertThat(loginApplicantResolver.supportsParameter(loginApplicantParameter)).isTrue()
    }

    @Test
    fun `요청에 담긴 loginApplicantEmail 속성으로 저장된 지원자를 불러온다`() {
        val nativeWebRequest = mockk<NativeWebRequest>()
        every {
            nativeWebRequest.getAttribute("loginApplicantEmail", SCOPE_REQUEST)
        } returns "applicant_email@email.com"

        val expectedApplicant = Applicant(
            name = "홍길동1",
            email = "applicant_email@email.com",
            phoneNumber = "010-0000-0000",
            gender = Gender.MALE,
            birthday = createLocalDate(2020, 4, 17),
            password = "password"
        )
        every { applicantService.findByEmail("applicant_email@email.com") } returns expectedApplicant

        val result =
            loginApplicantResolver.resolveArgument(loginApplicantParameter, null, nativeWebRequest, null)
        assertThat(result).usingRecursiveComparison().isEqualTo(expectedApplicant)
    }
}
