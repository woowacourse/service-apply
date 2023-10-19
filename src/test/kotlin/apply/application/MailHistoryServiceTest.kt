package apply.application

import apply.application.mail.MailHistoryService
import apply.createMailMessage
import apply.createSuccessMailHistory
import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.MailMessageRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import support.test.spec.afterRootTest

class MailHistoryServiceTest : BehaviorSpec({
    val mailHistoryRepository = mockk<MailHistoryRepository>()
    val mailMessageRepository = mockk<MailMessageRepository>()

    val mailHistoryService = MailHistoryService(mailHistoryRepository, mailMessageRepository)

    Given("메일 이력이 있는 경우") {
        val mailMessage = createMailMessage(id = 1L)

        every { mailHistoryRepository.findAll() } returns listOf(
            createSuccessMailHistory(mailMessage.id, listOf("a@a.com")),
            createSuccessMailHistory(mailMessage.id, listOf("b@b.com"))
        )

        every { mailMessageRepository.findAllById(any()) } returns listOf(mailMessage)

        When("모든 메일 이력을 조회하면") {
            val actual = mailHistoryService.findAll()

            Then("모든 메일 이력을 확인할 수 있다") {
                actual[0].recipients shouldContain "a@a.com"
                actual[1].recipients shouldContain "b@b.com"
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
