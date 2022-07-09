package apply.security

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.shouldBe

private const val PAYLOAD = "email@email.com"
private const val NOT_VALID_TOKEN = ""
private const val NEGATIVE_VALIDITY_TIME = -10L

internal class JwtTokenProviderTest : AnnotationSpec() {
    @Test
    fun `payload를 넣어 토큰을 생성하고 토큰에서 다시 payload를 불러올 수 있는지 확인한다`() {
        val jwtTokenProvider = JwtTokenProvider()
        val token = jwtTokenProvider.createToken(PAYLOAD)

        jwtTokenProvider.getSubject(token) shouldBe PAYLOAD
        // assertThat(jwtTokenProvider.getSubject(token)).isEqualTo(PAYLOAD)
    }

    @Test
    fun `유효시간이 지난 토큰의 유효성 검사가 실패한다`() {
        val jwtTokenProvider = JwtTokenProvider(expirationInMilliseconds = NEGATIVE_VALIDITY_TIME)
        val token = jwtTokenProvider.createToken(PAYLOAD)

        jwtTokenProvider.isValidToken(token).shouldBeFalse()
        // assertThat(jwtTokenProvider.isValidToken(token)).isFalse()
    }

    @Test
    fun `올바르지 않은 토큰의 유효성 검사가 실패한다`() {
        val jwtTokenProvider = JwtTokenProvider()

        jwtTokenProvider.isValidToken(NOT_VALID_TOKEN).shouldBeFalse()
        // assertThat(jwtTokenProvider.isValidToken(NOT_VALID_TOKEN)).isFalse()
    }
}
