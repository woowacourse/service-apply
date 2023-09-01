package apply.application.mail

import apply.config.TestMailConfig
import apply.createMailData
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.string.shouldContain
import org.springframework.test.context.ContextConfiguration
import support.test.IntegrationTest

@IntegrationTest
@ContextConfiguration(classes = [TestMailConfig::class])
class MailServiceTest(
    private val mailService: MailService
) : BehaviorSpec({

    Given("MailData를 사용해서 메일을 보내는 경우") {
        val body = "안녕하세요. 우아한테크코스입니다.\n" + "\n" +
            "1주 차 미션은 개발 환경을 세팅하고, GitHub에 과제를 제출하는 등 미션 외에도 추가로 익혀야 하는 부분들이 있어 가벼운 미션으로 준비했어요."
        val mailData = createMailData(body = body)

        When("common 메일 본문에 적용하면") {
            val commonMailBody = mailService.createCommonMailBodyFrom(mailData)

            Then("본문이 포함된 HTML로 작성된다.") {
                println(commonMailBody)
                commonMailBody shouldContain "<body><p>안녕하세요. 우아한테크코스입니다."
            }
        }
    }
})
