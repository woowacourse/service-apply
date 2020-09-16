package support.reflect

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow

internal class ClassesTest {
    @Test
    fun `애너테이션이 없는 클래스`() {
        class WithoutAnnotation

        assertThatExceptionOfType(IllegalStateException::class.java).isThrownBy {
            WithoutAnnotation::class.newInstance()
        }
    }

    @Test
    fun `애너테이션이 있는 클래스`() {
        @NoArgsConstructor
        class WithAnnotation

        assertDoesNotThrow {
            WithAnnotation::class.newInstance()
        }
    }

    @Test
    fun `기본값 확인`() {
        class Clazz

        @NoArgsConstructor
        class WithAnnotation(
            val int: Int,
            val double: Double,
            val string: String,
            val list: List<String>,
            val clazz: Clazz
        )

        val actual = WithAnnotation::class.newInstance()
        assertAll(
            { assertThat(actual.int).isZero() },
            { assertThat(actual.double).isZero() },
            { assertThat(actual.string).isNull() },
            { assertThat(actual.list).isNull() },
            { assertThat(actual.clazz).isNull() }
        )
    }

    @Test
    fun `이미 매개 변수가 없는 생성자가 있는 경우`() {
        @NoArgsConstructor
        class WithAnnotation(val int: Int = 0)

        assertThatExceptionOfType(IllegalStateException::class.java).isThrownBy {
            WithAnnotation::class.newInstance()
        }
    }
}
