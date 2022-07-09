package support.views

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.shouldBe

internal class IdFieldTest : AnnotationSpec() {
    @Test
    fun `값이 없으면 0`() {
        IdField().value.shouldBeZero()
    }

    @Test
    fun `유효한 값을 입력하는 경우`() {
        val actual = IdField().apply { value = 1L }
        actual.value shouldBe 1
    }
}
