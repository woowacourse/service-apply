package apply.security

import apply.application.UserService
import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.User
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.context.request.NativeWebRequest
import support.createLocalDate
import support.test.UnitTest
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.primaryConstructor

@UnitTest
internal class LoginUserResolverTest : DescribeSpec({

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

    fun createUserAnnotation(): LoginUser = LoginUser::class.createInstance()

    fun createAdministratorAnnotation(): LoginUser? = LoginUser::class.primaryConstructor?.call(true)

    describe("LoginUserResolver") {
        context("사용자 API를 호출하면") {

            every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createUserAnnotation()

            it("@LoginUser 주어진 메서드의 서포트 여부를 확인한다") {
                listOf(
                    "user" to true,
                    "administrator" to true,
                    "guest" to false
                ).forAll { (methodName, expected) ->
                    val method = TestAuthController::class.java.getDeclaredMethod(methodName, User::class.java)
                    val loginUserParameter: MethodParameter = MethodParameter.forExecutable(method, 0)
                    loginUserResolver.supportsParameter(loginUserParameter) shouldBe expected
                }
            }

            it("요청의 Authorization 헤더로 저장된 회원을 불러온다") {
                every { nativeWebRequest.getHeader(AUTHORIZATION) } returns "Bearer valid_token"
                every { jwtTokenProvider.isValidToken("valid_token") } returns true
                every { jwtTokenProvider.getSubject("valid_token") } returns "user_email@email.com"
                val expectedUser = User(
                    name = "홍길동1",
                    email = "user_email@email.com",
                    phoneNumber = "010-0000-0000",
                    gender = Gender.MALE,
                    birthday = createLocalDate(2020, 4, 17),
                    password = Password("password")
                )
                every { userService.getByEmail("user_email@email.com") } returns expectedUser

                val result = loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
                result shouldBeEqualToComparingFields expectedUser
            }

            it("요청의 Authorization 헤더의 형식이 올바르지 않을 경우 예외가 발생한다") {
                listOf(
                    "Bearertokeninfo",
                    "''",
                    "Bearer"
                ).forAll { header ->
                    every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createUserAnnotation()
                    every { nativeWebRequest.getHeader(AUTHORIZATION) } returns header

                    shouldThrowExactly<LoginFailedException> {
                        loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
                    }
                }
            }

            it("요청의 Authorization 헤더가 존재하지 않을 경우 예외가 발생한다") {
                every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createUserAnnotation()
                every { nativeWebRequest.getHeader(AUTHORIZATION) } returns null

                shouldThrowExactly<LoginFailedException> {
                    loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
                }
            }

            it("요청의 토큰이 유효하지 않은 경우 예외가 발생한다") {
                every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createUserAnnotation()
                every { nativeWebRequest.getHeader(AUTHORIZATION) } returns "invalid_token"
                every { jwtTokenProvider.isValidToken("invalid_token") } returns false

                shouldThrowExactly<LoginFailedException> {
                    loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
                }
            }
        }

        context("관리 API를 호출하면") {
            it("실패한다") {
                every { methodParameter.getParameterAnnotation<LoginUser>(any()) } returns createAdministratorAnnotation()
                shouldThrowExactly<LoginFailedException> {
                    loginUserResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
                }
            }
        }
    }
})
