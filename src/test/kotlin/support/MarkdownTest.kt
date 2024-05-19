package support

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldNotContain

class MarkdownTest : StringSpec({
    "마크다운을 HTML로 변환한다" {
        val markdownText = """
            |# 마크다운
            |
            |## 마크다운(Markdown)이란?
            |
            |2004년에 존 그루버(John Gruber)와 애런 스워츠(Aaron Swartz)가 만든 마크업 언어의 하나로 읽기 쉽고 쓰기 쉬운 텍스트 포맷입니다.
            |
            |이 페이지의 왼쪽은 마크다운 편집기입니다. **자유롭게 연습해 보세요**. 여러분이 연습한 내용은 다른 사람에게 보이지 않고, 저장되지 않습니다.
            |
            |아래 링크를 클릭하여 해당 도움말(연습장)로 바로 이동할 수도 있습니다.
            |
            |* [문단](#paragraph)
            |* [제목](#heading)
            |* [인용](#blockquote)
            |* [강조](#emphasis)
            |* [취소선](#strike)
            |* [목록](#list)
            |* [할일 목록](#tasklist)
            |* [링크](#link)
            |* [표](#table)
            |* [코드 포맷](#code)
            |* [수식](#math)
            |* [UML](#uml)
        """.trimMargin()
        val actual = markdownToHtml(markdownText)
        actual shouldBe """
            |<body>
            |<h1>마크다운</h1>
            |<h2>마크다운(Markdown)이란?</h2>
            |<p>2004년에 존 그루버(John Gruber)와 애런 스워츠(Aaron Swartz)가 만든 마크업 언어의 하나로 읽기 쉽고 쓰기 쉬운 텍스트 포맷입니다.</p>
            |<p>이 페이지의 왼쪽은 마크다운 편집기입니다. <strong>자유롭게 연습해 보세요</strong>. 여러분이 연습한 내용은 다른 사람에게 보이지 않고, 저장되지 않습니다.</p>
            |<p>아래 링크를 클릭하여 해당 도움말(연습장)로 바로 이동할 수도 있습니다.</p>
            |<ul>
            |<li><a href="#paragraph">문단</a></li>
            |<li><a href="#heading">제목</a></li>
            |<li><a href="#blockquote">인용</a></li>
            |<li><a href="#emphasis">강조</a></li>
            |<li><a href="#strike">취소선</a></li>
            |<li><a href="#list">목록</a></li>
            |<li><a href="#tasklist">할일 목록</a></li>
            |<li><a href="#link">링크</a></li>
            |<li><a href="#table">표</a></li>
            |<li><a href="#code">코드 포맷</a></li>
            |<li><a href="#math">수식</a></li>
            |<li><a href="#uml">UML</a></li>
            |</ul>
            |</body>
        """.flattenByMargin()
    }

    "마크다운을 <body> 태그를 제거한 HTML로 변환한다" {
        val markdownText = "2004년에 존 그루버(John Gruber)와 애런 스워츠(Aaron Swartz)가 만든 마크업 언어의 하나로 읽기 쉽고 쓰기 쉬운 텍스트 포맷입니다."
        val actual = markdownToEmbeddedHtml(markdownText)
        actual shouldNotContain "<body>"
    }
})
