package apply.domain.agreement

import apply.createAgreement
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.shouldBe
import support.test.RepositoryTest

@RepositoryTest
class AgreementRepositoryTest(
    private val agreementRepository: AgreementRepository,
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("동의서 조회") {
        agreementRepository.saveAll(
            listOf(
                createAgreement(version = 20240416),
                createAgreement(version = 20240506),
            )
        )

        expect("가장 최신 버전의 동의서를 조회한다") {
            val actual = agreementRepository.getFirstByOrderByVersionDesc()
            actual.version shouldBe 20240506
        }
    }

    context("동의서 조회 예외") {
        expect("동의서가 없으면 예외가 발생한다") {
            shouldThrow<NoSuchElementException> {
                agreementRepository.getFirstByOrderByVersionDesc()
            }
        }
    }
})
