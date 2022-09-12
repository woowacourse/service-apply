package support.views

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.shouldBe

class IdFieldTest : StringSpec({
    "값이 없으면 0" {
        IdField().value.shouldBeZero()
    }

    "유효한 값을 입력한 경우" {
        val actual = IdField().apply { value = 1L }
        actual.value shouldBe 1
    }
})
