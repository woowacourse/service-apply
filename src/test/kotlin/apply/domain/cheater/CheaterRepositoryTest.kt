package apply.domain.cheater

import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringTestExtension
import io.kotest.extensions.spring.SpringTestLifecycleMode
import io.kotest.matchers.booleans.shouldBeTrue
import support.test.RepositoryTest

@RepositoryTest
class CheaterRepositoryTest(
    private val cheaterRepository: CheaterRepository
) : ExpectSpec({
    extensions(SpringTestExtension(SpringTestLifecycleMode.Root))

    context("부정행위자 조회") {
        cheaterRepository.save(Cheater("a@email.com"))

        expect("지원자의 부정행위 여부를 확인한다") {
            cheaterRepository.existsByEmail("a@email.com").shouldBeTrue()
        }
    }
})
