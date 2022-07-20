package apply.application

import apply.createCheaterData
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.user.UserRepository
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import support.test.UnitTest

@UnitTest
internal class CheaterServiceTest : DescribeSpec({
    val userRepository: UserRepository = mockk()
    val cheaterRepository: CheaterRepository = mockk()
    val cheaterService: CheaterService = CheaterService(userRepository, cheaterRepository)

    describe("CheaterService") {
        it("부정 행위자를 추가한다") {
            val cheaterData = createCheaterData()
            every { cheaterRepository.existsByEmail(any()) } returns false
            every { cheaterRepository.save(any()) } returns Cheater(cheaterData.email, cheaterData.description)
            shouldNotThrowAny { cheaterService.save(cheaterData) }
        }

        context("이미 등록된 부정 행위자를 추가하는 경우") {
            it("예외를 던진다") {
                val cheaterData = createCheaterData()
                every { cheaterRepository.existsByEmail(any()) } returns true
                shouldThrow<IllegalArgumentException> { cheaterService.save(cheaterData) }
            }
        }
    }
})
