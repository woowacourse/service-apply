package apply.domain.user

import io.kotest.assertions.assertSoftly
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.assertj.core.api.Assertions.assertThat
import support.createLocalDate
import support.test.RepositoryTest

@RepositoryTest
internal class UserRepositoryTest(private val userRepository: UserRepository) : AnnotationSpec() {
    @BeforeEach
    internal fun setUp() {
        val users = listOf(
            User(
                "홍길동1",
                "a@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 4, 17),
                Password("password")
            ),
            User(
                "홍길동2",
                "b@email.com",
                "010-0000-0000",
                Gender.FEMALE,
                createLocalDate(2020, 5, 5),
                Password("password")
            ),
            User(
                "동해물과백두산이마르고닳도록하느님이보우하사우리나라만세무궁",
                "c@email.com",
                "010-0000-0000",
                Gender.MALE,
                createLocalDate(2020, 1, 1),
                Password("password")
            )
        )
        userRepository.saveAll(users)
    }

    @Test
    fun `이름 또는 이메일에 검색 키워드가 포함되는 회원들을 모두 조회한다`() {
        listOf(
            "홍" to 2,
            "a@" to 1,
            "" to 3,
            "4" to 0
        ).forAll { (keyword, expectedSize) ->
            val result = userRepository.findAllByKeyword(keyword)
            assertThat(result).hasSize(expectedSize)
        }
    }

    @Test
    fun `이메일이 일치하는 회원을 조회한다`() {
        userRepository.findByEmail("b@email.com")!!.name shouldBe "홍길동2"
    }

    @Test
    fun `이메일이 일치하는 회원이 존재하지 않을 때, null을 반환한다`() {
        userRepository.findByEmail("notexist@email.com").shouldBeNull()
    }

    @Test
    fun `이메일이 일치하는 회원들을 전부 조회한다`() {
        val emails = listOf("b@email.com", "c@email.com")
        userRepository.findAllByEmailIn(emails) shouldHaveSize 2
    }

    @Test
    fun `이메일이 일치하는 회원이 있는지 확인한다`() {
        assertSoftly {
            userRepository.existsByEmail("a@email.com").shouldBeTrue()
            userRepository.existsByEmail("non-exists@email.com").shouldBeFalse()
        }
    }
}
