package support

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.string.shouldContain

class MarkdownTest : StringSpec({
    "마크다운 문법으로 작성된 내용을 HTML로 변환할 수 있다" {
        val src = """
            안녕하세요. 우아한테크코스입니다.

            3주 차 미션에서는 2주 차에서 학습한 것에 2가지 목표를 추가했어요.

            1. 클래스(객체)를 분리하는 연습
            2. 도메인 로직에 대한 단위 테스트를 작성하는 연습
            
            ## 프리코스 진행방식

            ### 미션 제출 방법
            
            - 미션 구현을 완료한 후 GitHub을 통해 제출해야 한다.
            - GitHub을 활용한 제출 방법은 [문서](https://github.com/woowacourse/woowacourse-docs/tree/master/precourse)를 참고해 제출한다.
            - GitHub에 미션을 제출한 후 [우아한테크코스 지원 플랫폼](https://apply.techcourse.co.kr/)에서 프리코스 과제를 제출한다.
            - 지원 플랫폼에서 과제를 제출할 때 미션을 진행하며 경험한 내용에 대한 소감문을 작성한다.
        """.trimIndent()

        val html = markdownToHtml(src)

        html shouldContain "<ol><li>클래스(객체)를 분리하는 연습</li><li>도메인 로직에 대한 단위 테스트를 작성하는 연습</li></ol>"
        html shouldContain "<h2>프리코스 진행방식</h2>"
        html shouldContain "<h3>미션 제출 방법</h3>"
        html shouldContain "<li>GitHub에 미션을 제출한 후 <a href=\"https://apply.techcourse.co.kr/\">우아한테크코스 지원 플랫폼</a>에서 프리코스 과제를 제출한다.</li>"
    }
})
