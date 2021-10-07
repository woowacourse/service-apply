package apply.application

import apply.application.mail.MailData
import apply.application.mail.listToString
import apply.domain.mail.MailHistory
import apply.domain.mail.EmailHistoryRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import support.test.UnitTest
import java.time.LocalDateTime

@UnitTest
class MailHistoryServiceTest {
    @MockK
    private lateinit var emailHistoryRepository: EmailHistoryRepository

    private lateinit var mailHistoryService: MailHistoryService

    @BeforeEach
    internal fun setUp() {
        mailHistoryService = MailHistoryService(emailHistoryRepository)
    }

    // todo: make test fixture
    @Test
    fun `메일 이력을 저장한다`() {
        val recipients = listOf("test@email.com", "test2@email.com")
        val mailData = MailData("제목1", "본문입니다.", "woowacourse@woowa.com", recipients)
        every { emailHistoryRepository.save(any()) } returns MailHistory(
            "제목1",
            "본문입니다.",
            "woowacourse@woowa.com",
            recipients.joinToString()
        )
        assertDoesNotThrow { mailHistoryService.save(mailData) }
    }

    @Test
    fun `저장된 메일 이력을 모두 조회한다`() {
        val now = LocalDateTime.now()
        val recipients = listOf("test@email.com", "test2@email.com")
        val mailData1 = MailData("제목1", "본문입니다.", "woowacourse@woowa.com", recipients, now)
        val mailData2 = MailData("제목2", "본문입니다.", "woowacourse@woowa.com", recipients, now)
        val emailHistory1 = MailHistory("제목1", "본문입니다.", "woowacourse@woowa.com", recipients.listToString(), now)
        val emailHistory2 = MailHistory("제목2", "본문입니다.", "woowacourse@woowa.com", recipients.listToString(), now)
        val emailHistories = listOf(emailHistory1, emailHistory2)
        every { emailHistoryRepository.findAll() } returns emailHistories
        assertThat(mailHistoryService.findAll()).contains(mailData1, mailData2)
    }
}
