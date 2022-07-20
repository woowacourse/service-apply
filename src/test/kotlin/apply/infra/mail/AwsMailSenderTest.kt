package apply.infra.mail

import io.kotest.core.spec.style.DescribeSpec
import support.test.IntegrationTest

private const val ALWAYS_SUCCESSFUL_DELIVERY_ADDRESS: String = "success@simulator.amazonses.com"
private const val TEST_SUBJECT: String = "테스트"
private const val TEST_HTML_BODY: String =
    """<img src="https://woowacourse.github.io/img/logo_full_dark.26e30197.png" style="width: 200px" alt="logo" />"""

@IntegrationTest
internal class AwsMailSenderTest(
    private val awsMailSender: AwsMailSender
) : DescribeSpec({
    xdescribe("AwsMailSender") {
        it("성공적인 전송") {
            awsMailSender.send(ALWAYS_SUCCESSFUL_DELIVERY_ADDRESS, TEST_SUBJECT, TEST_HTML_BODY)
        }
    }
})
