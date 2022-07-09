package apply.application

import apply.createMailData
import apply.createMailHistory
import apply.domain.mail.MailHistoryRepository
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.mockk.every
import io.mockk.impl.annotations.MockK
import support.test.UnitTest
import java.time.LocalDateTime

@UnitTest
class MailHistoryServiceTest : AnnotationSpec() {
    @MockK
    private lateinit var mailHistoryRepository: MailHistoryRepository

    private lateinit var mailHistoryService: MailHistoryService

    @BeforeEach
    internal fun setUp() {
        mailHistoryService = MailHistoryService(mailHistoryRepository)
    }

    @Test
    fun `메일 이력을 저장한다`() {
        val mailData = createMailData()
        every { mailHistoryRepository.save(any()) } returns createMailHistory()
        shouldNotThrow<Exception> { mailHistoryService.save(mailData) }
    }

    @Test
    fun `저장된 메일 이력을 모두 조회한다`() {
        val now = LocalDateTime.now()
        val mailData1 = createMailData(subject = "제목1", sentTime = now)
        val mailData2 = createMailData(subject = "제목2", sentTime = now.plusSeconds(1))
        val emailHistory1 = createMailHistory(subject = "제목1", sentTime = now)
        val emailHistory2 = createMailHistory(subject = "제목2", sentTime = now.plusSeconds(1))
        every { mailHistoryRepository.findAll() } returns listOf(emailHistory1, emailHistory2)
        mailHistoryService.findAll().shouldContainExactly(mailData1, mailData2)
    }
}
