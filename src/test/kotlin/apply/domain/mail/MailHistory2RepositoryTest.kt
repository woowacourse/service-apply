package apply.domain.mail

import apply.createMailMessage
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.longs.shouldNotBeZero
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import support.test.RepositoryTest
import support.test.spec.afterRootTest

@RepositoryTest
class MailHistory2RepositoryTest(
    private val mailMessageRepository: MailMessageRepository,
    private val mailHistory2Repository: MailHistory2Repository,
    private val entityManager: TestEntityManager
) : ExpectSpec({
    extensions(SpringExtension)

    context("메일 히스토리 저장") {
        val mailMessage = mailMessageRepository.save(createMailMessage())

        expect("메일 발송 성공에 대한 히스토리를 저장한다") {
            val actual = mailHistory2Repository.save(MailHistory2.ofSuccess(mailMessage, mailMessage.recipients))
            actual.id.shouldNotBeZero()
        }
    }

    context("메일 히스토리 조회") {
        val mailMessage = mailMessageRepository.save(createMailMessage())
        mailHistory2Repository.save(MailHistory2.ofSuccess(mailMessage, mailMessage.recipients))

        expect("메일 발송 성공에 대한 히스토리를 저장한다") {
            val actual = mailHistory2Repository.findAll()
            actual.shouldNotBeEmpty()
            actual[0].mailMessage.id.shouldNotBeZero()
        }
    }

    afterEach {
        entityManager.flush()
        entityManager.clear()
    }

    afterRootTest {
        mailHistory2Repository.deleteAll()
    }
})
