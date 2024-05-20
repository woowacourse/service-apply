package study

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class IndentTest : StringSpec({
    "여러 줄 문자열에서 선행 공백을 제거하고 줄바꿈을 인식한다" {
        val actual =
            """
                <html>
                </html>
            """.trimIndent()
        actual shouldBe "<html>\n</html>"
    }

    "선행 공백이 가장 적은 줄을 기준으로 그보다 많은 공백이 있으면 실제 공백으로 인식한다" {
        val actual =
            """
                <p>
            동해물과 백두산이
                </p>
            """.trimIndent()
        actual shouldBe "    <p>\n동해물과 백두산이\n    </p>"
    }

    "줄 시작 기준을 파이프 기호로 변경한다" {
        val actual =
            """
                |<html>
                |</html>
            """.trimMargin()
        actual shouldBe "<html>\n</html>"
    }

    "파이프 기호를 기반으로 하므로 다른 줄의 영향을 받지 않는다" {
        val actual =
            """
                |<p>
            |동해물과 백두산이
                |</p>
            """.trimMargin()
        actual shouldBe "<p>\n동해물과 백두산이\n</p>"
    }

    "파이프 기호 뒤의 공백을 실제 공백으로 인식한다" {
        val actual =
            """
                |<html>
                |  <body>
                |  </body>
                |</html>
            """.trimMargin()
        actual.shouldContain("<html>\n  <body>\n  </body>\n</html>")
    }

    "줄 시작 부분의 파이프 기호를 다른 문자로 변경한다" {
        val actual =
            """
                |<html>
                |</html>
            """.replaceIndentByMargin("*")
        actual shouldBe "*<html>\n*</html>"
    }

    "줄 시작 부분의 파이프 기호를 빈 문자로 변경한다" {
        val text =
            """
                |<html>
                |</html>
            """
        val actual = text.replaceIndentByMargin("")
        val expected = text.trimMargin()
        actual shouldBe expected
    }
})
