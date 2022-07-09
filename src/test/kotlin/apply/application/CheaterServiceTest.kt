package apply.application

import apply.createCheaterData
import apply.domain.cheater.Cheater
import apply.domain.cheater.CheaterRepository
import apply.domain.user.UserRepository
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.AnnotationSpec
import io.mockk.every
import io.mockk.impl.annotations.MockK
import support.test.UnitTest

@UnitTest
internal class CheaterServiceTest : AnnotationSpec() {
    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var cheaterRepository: CheaterRepository
    private lateinit var cheaterService: CheaterService

    @BeforeEach
    internal fun setUp() {
        cheaterService = CheaterService(userRepository, cheaterRepository)
    }

    @Test
    fun `부정 행위자를 추가한다`() {
        val cheaterData = createCheaterData()
        every { cheaterRepository.existsByEmail(any()) } returns false
        every { cheaterRepository.save(any()) } returns Cheater(cheaterData.email, cheaterData.description)
        shouldNotThrow<Exception> { cheaterService.save(cheaterData) }
    }

    @Test
    fun `이미 등록된 부정 행위자를 추가하는 경우 예외를 던진다`() {
        val cheaterData = createCheaterData()
        every { cheaterRepository.existsByEmail(any()) } returns true
        shouldThrow<IllegalArgumentException> { cheaterService.save(cheaterData) }
    }
}
