package apply.application

import apply.application.mail.MailHistoryService
import apply.createSuccessMailHistory
import apply.domain.mail.MailHistoryRepository
import apply.domain.mail.MailMessageRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import support.test.spec.afterRootTest

class MailHistoryServiceTest : BehaviorSpec({
    val mailHistoryRepository = mockk<MailHistoryRepository>()
    val mailMessageRepository = mockk<MailMessageRepository>()

    val mailHistoryService = MailHistoryService(mailHistoryRepository, mailMessageRepository)

    Given("메일 이력이 있는 경우") {
        every { mailHistoryRepository.findAll() } returns listOf(
            createSuccessMailHistory(subject = "제목1"),
            createSuccessMailHistory(subject = "제목2")
        )

        When("모든 메일 이력을 조회하면") {
            val actual = mailHistoryService.findAll()

            Then("모든 메일 이력을 확인할 수 있다") {
                actual[0].subject shouldBe "제목1"
                actual[1].subject shouldBe "제목2"
            }
        }
    }

    afterRootTest {
        clearAllMocks()
    }
})
