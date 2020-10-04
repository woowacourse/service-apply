package support.views

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class IdFieldTest {
    @Test
    fun `값이 없으면 0`() {
        assertThat(IdField().value).isZero()
    }

    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = IdField().apply { value = 1L }
        assertThat(actual.value).isOne()
    }
}
