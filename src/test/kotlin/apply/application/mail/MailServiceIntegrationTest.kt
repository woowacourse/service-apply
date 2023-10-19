package apply.application.mail

import apply.config.TestMailConfiguration
import apply.createMailData
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldContain
import org.springframework.context.annotation.Import
import support.test.IntegrationTest

@Import(TestMailConfiguration::class)
@IntegrationTest
class MailServiceIntegrationTest(
    private val sendingMailService: SendingMailService
) : BehaviorSpec({
    Given("마크다운으로 본문을 작성한 이메일이 있는 경우") {
        val body = """
            |안녕하세요. 우아한테크코스입니다.  
            |오늘은 미션 안내와 더불어 2주 차부터 시작되는 커뮤니티에 대한 가이드도 함께 공유드리니 **반드시 이메일 내용을 꼼꼼하게 정독**해 주세요.
        """.trimMargin()
        val mailData = createMailData(body = body)

        When("이메일 본문을 생성하면") {
            val actual = sendingMailService.generateMailBody(mailData)

            Then("본문이 HTML로 변환된 이메일이 생성된다") {
                actual shouldContain "<title>email</title>"
                actual shouldContain """
                    |<p>안녕하세요. 우아한테크코스입니다.<br />
                    |오늘은 미션 안내와 더불어 2주 차부터 시작되는 커뮤니티에 대한 가이드도 함께 공유드리니 <strong>반드시 이메일 내용을 꼼꼼하게 정독</strong>해 주세요.</p>
                """.trimMargin()
            }
        }
    }
})
