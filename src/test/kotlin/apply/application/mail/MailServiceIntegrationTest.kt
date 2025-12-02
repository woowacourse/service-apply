package apply.application.mail

import apply.config.TestMailConfiguration
import apply.createMailData
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldContain
import org.springframework.context.annotation.Import
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.ISpringTemplateEngine
import support.test.IntegrationTest
import java.time.LocalDate

@Import(TestMailConfiguration::class)
@IntegrationTest
class MailServiceIntegrationTest(
    private val mailService: MailService,
    private val templateEngine: ISpringTemplateEngine,
) : BehaviorSpec({
    Given("마크다운으로 본문을 작성한 이메일이 있는 경우") {
        val body = """
            |안녕하세요. 우아한테크코스입니다.  
            |오늘은 미션 안내와 더불어 2주 차부터 시작되는 커뮤니티에 대한 가이드도 함께 공유드리니 **반드시 이메일 내용을 꼼꼼하게 정독**해 주세요.
        """.trimMargin()
        val mailData = createMailData(body = body)

        When("이메일 본문을 생성하면") {
            val actual = mailService.generateMailBody(mailData)

            Then("본문이 HTML로 변환된 이메일이 생성된다") {
                actual shouldContain "<title>email</title>"
                actual shouldContain """
                    |<p>안녕하세요. 우아한테크코스입니다.<br />
                    |오늘은 미션 안내와 더불어 2주 차부터 시작되는 커뮤니티에 대한 가이드도 함께 공유드리니 <strong>반드시 이메일 내용을 꼼꼼하게 정독</strong>해 주세요.</p>
                """.trimMargin()
            }
        }
    }

    Given("이메일 템플릿이 있는 경우") {
        val context = Context().apply {
            setVariables(
                mapOf(
                    "name" to "홍길동",
                    "recruit" to "우아한테크코스 웹 백엔드 8기",
                    "url" to "https://apply.techcourse.co.kr",
                )
            )
        }

        When("템플릿 엔진으로 이메일을 렌더링하면") {
            val actual = templateEngine.process("mail/submission-complete", context)
                .replace("\\s+".toRegex(), " ")
                .replace("<(\\w+)\\s+>".toRegex(), "<$1>")
                .replace("</(\\w+)\\s+>".toRegex(), "</$1>")

            Then("이메일 본문이 생성된다") {
                actual shouldContain "Hello <span>홍길동</span>,"
                actual shouldContain "안녕하세요, <span>홍길동</span>님."
                actual shouldContain "Copyright <span>${LocalDate.now().year}</span>."
            }
        }
    }
})
