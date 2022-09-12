package apply.security

import apply.application.UserService
import apply.createUser
import apply.domain.user.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.context.request.NativeWebRequest

class LoginUserResolverTest : StringSpec({
    val userService = mockk<UserService>()
    val jwtTokenProvider = mockk<JwtTokenProvider>()
    val methodParameter = mockk<MethodParameter>()
    val nativeWebRequest = mockk<NativeWebRequest>()

    val loginUserResolver = LoginUserResolver(jwtTokenProvider, userService)

    class TestAuthController {
        fun user(@LoginUser user: User) {}
        fun administrator(@LoginUser(administrator = true) user: User) {}
        fun guest(user: User) {}
    }

    fun createAdministratorAnnotation(): LoginUser = LoginUser(administrator = true)
    fun createUserAnnotation(): LoginUser = LoginUser(administrator = false)

    "주어진 함수가 @LoginUser를 지원하는지 확인한다" {
        listOf("user" to true, "administrator" to true, "guest" to false).forAll { (methodName, expected) ->
            val method = TestAuthController::class.java.getDeclaredMethod(methodName, User::class.java)
            val loginUserParameter = MethodParameter.forExecutable(method, 0)
            loginUserResolver.supportsParameter(loginUserParameter) shouldBe expected
        }
    }

    "관리 API를 호출하면 실패한다" {
        every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createAdministratorAnnotation()
        shouldThrow<LoginFailedException> {
            loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        }
    }

    "요청의 Authorization 헤더로 저장된 회원을 불러온다" {
        every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createUserAnnotation()
        every { nativeWebRequest.getHeader(AUTHORIZATION) } returns "Bearer valid_token"
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns "user_email@email.com"
        every { userService.getByEmail("user_email@email.com") } returns createUser()

        val result = loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        result shouldBeEqualToComparingFields createUser()
    }

    "요청의 Authorization 헤더의 형식이 올바르지 않을 경우 예외가 발생한다" {
        listOf("Bearertokeninfo", "", "Bearer").forAll { header ->
            every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createUserAnnotation()
            every { nativeWebRequest.getHeader(AUTHORIZATION) } returns header

            shouldThrow<LoginFailedException> {
                loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
            }
        }
    }

    "요청의 Authorization 헤더가 존재하지 않을 경우 예외가 발생한다" {
        every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createUserAnnotation()
        every { nativeWebRequest.getHeader(AUTHORIZATION) } returns null

        shouldThrow<LoginFailedException> {
            loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        }
    }

    "요청의 토큰이 유효하지 않은 경우 예외가 발생한다" {
        every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createUserAnnotation()
        every { nativeWebRequest.getHeader(AUTHORIZATION) } returns "invalid_token"
        every { jwtTokenProvider.isValidToken("invalid_token") } returns false

        shouldThrow<LoginFailedException> {
            loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        }
    }
})
