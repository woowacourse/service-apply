package apply.application.tobe

import apply.application.MailHistoryService
import apply.createMailData
import apply.createMailHistory
import apply.domain.mail.MailHistoryRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime.now

class MailHistoryServiceTest : BehaviorSpec({
    val mailHistoryRepository = mockk<MailHistoryRepository>()

    val mailHistoryService = MailHistoryService(mailHistoryRepository)

    Given("메일 이력이 있는 경우") {
        val now = now()

        every { mailHistoryRepository.findAll() } returns listOf(
            createMailHistory(subject = "제목1", sentTime = now),
            createMailHistory(subject = "제목2", sentTime = now.plusSeconds(1))
        )

        When("모든 메일 이력을 조회하면") {
            val actual = mailHistoryService.findAll()

            Then("모든 메일 이력을 확인할 수 있다") {
                actual[0] shouldBe createMailData(subject = "제목1", sentTime = now)
                actual[1] shouldBe createMailData(subject = "제목2", sentTime = now.plusSeconds(1))
            }
        }
    }
})
