package apply.infra.mail

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor

private const val ALWAYS_SUCCESSFUL_DELIVERY_ADDRESS: String = "success@simulator.amazonses.com"
private const val TEST_SUBJECT: String = "테스트"
private const val TEST_HTML_BODY: String =
    """<img src="https://woowacourse.github.io/img/logo_full_dark.26e30197.png" style="width: 200px" alt="logo" />"""

@Disabled
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class AwsMailSenderTest(
    private val awsMailSender: AwsMailSender
) {
    @Test
    fun `성공적인 전송`() {
        awsMailSender.send(ALWAYS_SUCCESSFUL_DELIVERY_ADDRESS, TEST_SUBJECT, TEST_HTML_BODY)
    }
}
