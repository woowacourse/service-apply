package apply.domain.cheater

import apply.domain.user.Gender
import apply.domain.user.Password
import apply.domain.user.User
import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import support.createLocalDate
import support.test.RepositoryTest

@RepositoryTest
internal class CheaterRepositoryTest(
    private val cheaterRepository: CheaterRepository
) : DescribeSpec({
    val cheater = User(
        id = 1L,
        name = "홍길동1",
        email = "a@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17),
        password = Password("password")
    )

    val user = User(
        id = 2L,
        name = "홍길동2",
        email = "b@email.com",
        phoneNumber = "010-0000-0000",
        gender = Gender.MALE,
        birthday = createLocalDate(2020, 4, 17),
        password = Password("password")
    )

    describe("CheaterRepository") {
        context("지원자의 부정행위 정보가 저장되면") {
            cheaterRepository.save(Cheater(cheater.email))

            it("부정 행위 여부를 확인한다") {
                assertSoftly(cheaterRepository) {
                    existsByEmail(cheater.email).shouldBeTrue()
                    existsByEmail(user.email).shouldBeFalse()
                }
            }
        }
    }
})
