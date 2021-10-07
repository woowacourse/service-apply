package apply.application

import apply.createMailData
import apply.createMailHistory
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

    @Test
    fun `메일 이력을 저장한다`() {
        val mailData = createMailData()
        every { emailHistoryRepository.save(any()) } returns createMailHistory()
        assertDoesNotThrow { mailHistoryService.save(mailData) }
    }

    @Test
    fun `저장된 메일 이력을 모두 조회한다`() {
        val now = LocalDateTime.now()
        val mailData1 = createMailData(subject = "제목1", sentTime = now)
        val mailData2 = createMailData(subject = "제목2", sentTime = now.plusSeconds(1))
        val emailHistory1 = createMailHistory(subject = "제목1", sentTime = now)
        val emailHistory2 = createMailHistory(subject = "제목2", sentTime = now.plusSeconds(1))
        every { emailHistoryRepository.findAll() } returns listOf(emailHistory1, emailHistory2)
        assertThat(mailHistoryService.findAll()).containsExactly(mailData1, mailData2)
    }
}
