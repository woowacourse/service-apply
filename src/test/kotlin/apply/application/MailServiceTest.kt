package apply.application

import apply.createMailData
import apply.createMailHistory
import apply.createUser
import apply.domain.mail.MailHistoryRepository
import apply.domain.user.UserRepository
import apply.domain.user.findByEmail
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import support.test.UnitTest
import support.views.NO_NAME
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

    @Test
    fun `회원 중에서 해당하는 이메일이 있으면 이름과 이메일을 가져온다`() {
        val user = createUser()
        every { userRepository.findByEmail(user.email) } returns user
        val actual = mailService.findMailTargetByEmail(user.email)
        assertThat(actual.name).isEqualTo(user.name)
    }

    @Test
    fun `회원 중에서 해당하는 이메일이 없으면 (이름없음)과 이메일을 반환한다`() {
        val email = "email@email.com"
        every { userRepository.findByEmail(email) } returns null
        val actual = mailService.findMailTargetByEmail(email)
        assertThat(actual.name).isEqualTo(NO_NAME)
    }
}
