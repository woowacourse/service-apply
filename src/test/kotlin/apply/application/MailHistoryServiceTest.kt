package apply.application

import apply.createMailData
import apply.createMailHistory
import apply.createUser
import apply.domain.mail.MailHistoryRepository
import apply.domain.user.UserRepository
import apply.domain.user.findAllByEmailIn
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
    private lateinit var mailHistoryRepository: MailHistoryRepository

    @MockK
    private lateinit var userRepository: UserRepository

    private lateinit var mailHistoryService: MailHistoryService

    @BeforeEach
    internal fun setUp() {
        mailHistoryService = MailHistoryService(mailHistoryRepository, userRepository)
    }

    @Test
    fun `메일 이력을 저장한다`() {
        val mailData = createMailData()
        every { mailHistoryRepository.save(any()) } returns createMailHistory()
        assertDoesNotThrow { mailHistoryService.save(mailData) }
    }

    @Test
    fun `저장된 메일 이력을 모두 조회한다`() {
        val now = LocalDateTime.now()
        val mailData1 = createMailData(subject = "제목1", sentTime = now)
        val mailData2 = createMailData(subject = "제목2", sentTime = now.plusSeconds(1))
        val emailHistory1 = createMailHistory(subject = "제목1", sentTime = now)
        val emailHistory2 = createMailHistory(subject = "제목2", sentTime = now.plusSeconds(1))
        every { mailHistoryRepository.findAll() } returns listOf(emailHistory1, emailHistory2)
        assertThat(mailHistoryService.findAll()).containsExactly(mailData1, mailData2)
    }

    @Test
    fun `이메일 중에서 회원에 해당하는 이메일이 있으면 (회원이름, 이메일)을, 회원에 해당하는 이메일이 없으면 (공백, 이메일)을 반환한다`() {
        val users = listOf(
            createUser(name = "회원1", email = "test1@email.com"),
            createUser(name = "회원3", email = "test3@email.com")
        )
        val emails = listOf("test1@email.com", "test2@email.com", "test3@email.com")
        val mailTargetResponses = listOf(
            MailTargetResponse("test1@email.com", "회원1"),
            MailTargetResponse("test3@email.com", "회원3"),
            MailTargetResponse("test2@email.com")
        )
        every { userRepository.findAllByEmailIn(emails) } returns users
        val actual = mailHistoryService.findAllMailTargetsByEmails(emails)
        assertThat(actual).isEqualTo(mailTargetResponses)
    }
}
