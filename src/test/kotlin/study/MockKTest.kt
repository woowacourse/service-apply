package study

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import study.Bar.bar

class Foo {
    fun String.foo(value: String) {}

    fun foo(value: String) {
        throw RuntimeException()
    }
}

object Bar {
    fun String.bar(value: String) {}
}

fun String.baz(value: String) {}

fun Foo.baz(value: String) {
    println("baz")
    foo(value)
}

class MockKTest : StringSpec({
    "다른 클래스에서 선언된 확장 함수를 스텁할 수 있다" {
        val mockk = mockk<Foo>()
        with(mockk) {
            every { "first".foo(any()) } answers {
                firstArg<String>() shouldBe "first"
                secondArg<String>() shouldBe "second"
            }
            "first".foo("second")
        }
    }

    "객체에 선언된 확장 함수를 스텁할 수 있다" {
        mockkObject(Bar)
        every { "first".bar(any()) } answers {
            firstArg<String>() shouldBe "first"
            secondArg<String>() shouldBe "second"
        }
        "first".bar("second")
        unmockkObject(Bar)
    }

    "최상위 확장 함수를 스텁할 수 있다" {
        mockkStatic("study.MockKTestKt")
        every { "first".baz("second") } answers {
            firstArg<String>() shouldBe "first"
            secondArg<String>() shouldBe "second"
        }
        "first".baz("second")
        unmockkStatic("study.MockKTestKt")
    }

    "최상위 확장 함수가 아닌 내부에서 호출되는 모의 객체의 함수를 스텁한다" {
        val foo = mockk<Foo>()
        every { foo.baz(any()) } answers {
            firstArg<String>() shouldBe "first"
        }
        shouldNotThrowAny { foo.foo("first") }
        verify { foo.foo(any()) }
    }
})
