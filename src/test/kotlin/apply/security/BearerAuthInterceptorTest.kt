package apply.security

import apply.domain.applicant.Applicant
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatIllegalArgumentException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.web.method.HandlerMethod
import kotlin.reflect.jvm.javaMethod

@ExtendWith(MockKExtension::class)
internal class BearerAuthInterceptorTest {

    @MockK
    private lateinit var jwtTokenProvider: JwtTokenProvider

    private lateinit var bearerAuthInterceptor: BearerAuthInterceptor

    private lateinit var mockHttpServletRequest: MockHttpServletRequest

    private lateinit var mockHttpServletResponse: MockHttpServletResponse

    private lateinit var authTargetMethod: HandlerMethod

    private lateinit var nonAuthTargetMethod: HandlerMethod

    @BeforeEach
    internal fun setUp() {
        bearerAuthInterceptor = BearerAuthInterceptor(jwtTokenProvider)
        mockHttpServletRequest = MockHttpServletRequest()
        mockHttpServletResponse = MockHttpServletResponse()
        authTargetMethod = HandlerMethod(TestAuthController(), TestAuthController::authTargetMethod.javaMethod!!)
        nonAuthTargetMethod = HandlerMethod(TestAuthController(), TestAuthController::nonAuthTargetMethod.javaMethod!!)
    }

    @Test
    fun `handler 메서드 파라미터에 @LoginApplicant 어노테이션이 없으면 Bearer 토큰을 추출하지 않고 통과한다`() {
        val result =
            bearerAuthInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, nonAuthTargetMethod)

        assertAll(
            { assertThat(mockHttpServletRequest.getAttribute(APPLICANT_EMAIL_ATTRIBUTE_NAME)).isNull() },
            { assertThat(result).isTrue() }
        )
    }

    @Test
    fun `handler 메서드 파라미터에 @LoginApplicant 어노테이션이 있으면 Bearer 토큰을 추출하고 통과한다`() {
        mockHttpServletRequest.addHeader("Authorization", "Bearer valid_token")
        every { jwtTokenProvider.getSubject("valid_token") } returns "applicant_email@email.com"

        val result =
            bearerAuthInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, authTargetMethod)

        assertAll(
            { assertThat(mockHttpServletRequest.getAttribute(APPLICANT_EMAIL_ATTRIBUTE_NAME)).isEqualTo("applicant_email@email.com") },
            { assertThat(result).isTrue() }
        )
    }

    @ParameterizedTest
    @CsvSource(
        "Authorization,Bearertokeninfo",
        "NonAuthorization,Bearer valid_token"
    )
    fun `handler 메서드 파라미터에 @LoginApplicant 어노테이션이 있고 토큰 형식이 유효하지 않을 경우 예외가 발생한다`(headerName: String, token: String) {
        mockHttpServletRequest.addHeader(headerName, token)

        assertThatIllegalArgumentException().isThrownBy {
            bearerAuthInterceptor.preHandle(mockHttpServletRequest, mockHttpServletResponse, authTargetMethod)
        }.withMessage("로그인 정보가 정확하지 않습니다")
    }
}

internal class TestAuthController {
    fun authTargetMethod(@LoginApplicant applicant: Applicant) {}

    fun nonAuthTargetMethod(applicant: Applicant) {}
}
