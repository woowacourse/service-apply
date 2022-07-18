package support.views

import io.kotest.core.spec.style.StringSpec
import org.assertj.core.api.Assertions.assertThat

class IdFieldTest : StringSpec({
    "값이 없으면 0" {
        assertThat(IdField().value).isZero()
    }

    "유효한 값을 입력하는 경우" {
        val actual = IdField().apply { value = 1L }
        assertThat(actual.value).isOne()
    }
})
