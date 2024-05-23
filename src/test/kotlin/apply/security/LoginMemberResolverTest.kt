package apply.security

import apply.application.MemberService
import apply.createMember
import apply.domain.member.Member
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.web.context.request.NativeWebRequest

class LoginMemberResolverTest : StringSpec({
    val memberService = mockk<MemberService>()
    val jwtTokenProvider = mockk<JwtTokenProvider>()
    val methodParameter = mockk<MethodParameter>()
    val nativeWebRequest = mockk<NativeWebRequest>()

    val loginMemberResolver = LoginMemberResolver(jwtTokenProvider, memberService)

    class TestAuthController {
        fun member(@LoginMember member: Member) {}
        fun administrator(@LoginMember(administrator = true) member: Member) {}
        fun guest(member: Member) {}
    }

    fun createAdministratorAnnotation(): LoginMember = LoginMember(administrator = true)
    fun createMemberAnnotation(): LoginMember = LoginMember(administrator = false)

    "주어진 함수가 @LoginMember를 지원하는지 확인한다" {
        listOf("member" to true, "administrator" to true, "guest" to false).forAll { (methodName, expected) ->
            val method = TestAuthController::class.java.getDeclaredMethod(methodName, Member::class.java)
            val loginMemberParameter = MethodParameter.forExecutable(method, 0)
            loginMemberResolver.supportsParameter(loginMemberParameter) shouldBe expected
        }
    }

    "관리 API를 호출하면 실패한다" {
        every { methodParameter.getParameterAnnotation<LoginMember>(any()) } returns createAdministratorAnnotation()
        shouldThrow<LoginFailedException> {
            loginMemberResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        }
    }

    "요청의 Authorization 헤더로 저장된 회원을 불러온다" {
        val member = createMember()

        every { methodParameter.getParameterAnnotation<LoginMember>(any()) } returns createMemberAnnotation()
        every { nativeWebRequest.getHeader(AUTHORIZATION) } returns "Bearer valid_token"
        every { jwtTokenProvider.isValidToken("valid_token") } returns true
        every { jwtTokenProvider.getSubject("valid_token") } returns member.email
        every { memberService.getByEmail(member.email) } returns member

        val actual = loginMemberResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        actual shouldBe member
    }

    "요청의 Authorization 헤더의 형식이 올바르지 않을 경우 예외가 발생한다" {
        listOf("Bearertokeninfo", "", "Bearer").forAll { header ->
            every { methodParameter.getParameterAnnotation<LoginMember>(any()) } returns createMemberAnnotation()
            every { nativeWebRequest.getHeader(AUTHORIZATION) } returns header

            shouldThrow<LoginFailedException> {
                loginMemberResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
            }
        }
    }

    "요청의 Authorization 헤더가 존재하지 않을 경우 예외가 발생한다" {
        every { methodParameter.getParameterAnnotation<LoginMember>(any()) } returns createMemberAnnotation()
        every { nativeWebRequest.getHeader(AUTHORIZATION) } returns null

        shouldThrow<LoginFailedException> {
            loginMemberResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        }
    }

    "요청의 토큰이 유효하지 않은 경우 예외가 발생한다" {
        every { methodParameter.getParameterAnnotation<LoginMember>(any()) } returns createMemberAnnotation()
        every { nativeWebRequest.getHeader(AUTHORIZATION) } returns "invalid_token"
        every { jwtTokenProvider.isValidToken("invalid_token") } returns false

        shouldThrow<LoginFailedException> {
            loginMemberResolver.resolveArgument(methodParameter, null, nativeWebRequest, null)
        }
    }
})
