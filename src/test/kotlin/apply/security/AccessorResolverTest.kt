package apply.security

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.core.MethodParameter
import org.springframework.web.context.request.NativeWebRequest
import java.nio.charset.StandardCharsets
import java.util.Base64

class AccessorResolverTest : StringSpec({

    val methodParameter = mockk<MethodParameter>()
    val webRequest = mockk<NativeWebRequest>()

    val accessorProperties = AccessorProperties(mapOf("lambda" to "lambda-key"))
    val accessorResolver = AccessorResolver(accessorProperties)

    class TestController {
        fun lambda(@Accessor("lambda") ignored: Unit) {}
        fun none(ignored: Unit) {}
    }

    "주어진 메소드가 @Accessor를 지원하는지 확인한다" {
        listOf("lambda" to true, "none" to false).forAll { (methodName, expected) ->
            val method = TestController::class.java.getDeclaredMethod(methodName, Unit::class.java)
            val methodParameter = MethodParameter.forExecutable(method, 0)
            accessorResolver.supportsParameter(methodParameter) shouldBe expected
        }
    }

    "Authorization 헤더 값과 accessor 키 값이 일치하면 성공한다" {
        val encoded = Base64.getEncoder().encode("lambda:lambda-key".toByteArray()).toString(StandardCharsets.UTF_8)
        every { webRequest.getHeader(any()) } returns "Basic $encoded"
        every { methodParameter.getParameterAnnotation<Accessor>(any()) } returns Accessor("lambda")

        shouldNotThrowAny {
            accessorResolver.resolveArgument(methodParameter, null, webRequest, null)
        }
    }

    "Authorization 헤더 값과 accessor의 username 값이 일치하지 않으면 실패한다" {
        val encoded = Base64.getEncoder().encode("invalid:lambda-key".toByteArray()).toString(StandardCharsets.UTF_8)
        every { webRequest.getHeader(any()) } returns "Basic $encoded"
        every { methodParameter.getParameterAnnotation<Accessor>(any()) } returns Accessor("lambda")

        shouldThrow<LoginFailedException> {
            accessorResolver.resolveArgument(methodParameter, null, webRequest, null)
        }
    }

    "Authorization 헤더 값과 accessor의 password 값이 일치하지 않으면 실패한다" {
        val encoded = Base64.getEncoder().encode("lambda:invalid-key".toByteArray()).toString(StandardCharsets.UTF_8)
        every { webRequest.getHeader(any()) } returns "Basic $encoded"
        every { methodParameter.getParameterAnnotation<Accessor>(any()) } returns Accessor("lambda")

        shouldThrow<LoginFailedException> {
            accessorResolver.resolveArgument(methodParameter, null, webRequest, null)
        }
    }

    "Authorization 헤더가 없으면 실패한다" {
        every { webRequest.getHeader(any()) } returns null

        shouldThrow<LoginFailedException> {
            accessorResolver.resolveArgument(methodParameter, null, webRequest, null)
        }
    }
})
