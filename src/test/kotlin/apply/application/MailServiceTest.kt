package apply.application

import apply.createMailData
import apply.createMailHistory
import apply.domain.mail.MailHistoryRepository
import apply.domain.user.UserRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import support.test.UnitTest
import java.time.LocalDateTime

@UnitTest
class MailServiceTest {
    @MockK
    private lateinit var mailHistoryRepository: MailHistoryRepository

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var mailService: MailService

    @BeforeEach
    internal fun setUp() {
        mailService = MailService(mailHistoryRepository, userRepository)
    }

    @Test
    fun `메일 이력을 저장한다`() {
        val mailData = createMailData()
        every { mailHistoryRepository.save(any()) } returns createMailHistory()
        assertDoesNotThrow { mailService.save(mailData) }
    }

    @Test
    fun `저장된 메일 이력을 모두 조회한다`() {
        val now = LocalDateTime.now()
        val mailData1 = createMailData(subject = "제목1", sentTime = now)
        val mailData2 = createMailData(subject = "제목2", sentTime = now.plusSeconds(1))
        val emailHistory1 = createMailHistory(subject = "제목1", sentTime = now)
        val emailHistory2 = createMailHistory(subject = "제목2", sentTime = now.plusSeconds(1))
        every { mailHistoryRepository.findAll() } returns listOf(emailHistory1, emailHistory2)
        assertThat(mailService.findAll()).containsExactly(mailData1, mailData2)
    }
}
