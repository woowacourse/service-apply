package apply.domain.member

import apply.NEW_PASSWORD
import apply.PASSWORD
import apply.createMember
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.repository.findByIdOrNull
import support.test.RepositoryTest
import support.test.spec.afterRootTest

@RepositoryTest
class MemberRepositoryTest(
    private val memberRepository: MemberRepository,
    private val entityManager: TestEntityManager,
) : ExpectSpec({
    extensions(SpringExtension)

    context("회원 조회") {
        memberRepository.saveAll(
            listOf(
                createMember(name = "홍길동1", email = "a@email.com"),
                createMember(name = "홍길동2", email = "b@email.com"),
                createMember(name = "동해물과백두산이마르고닳도록하느님이보우하사우리나라만세무궁", email = "c@email.com")
            )
        )

        expect("아이디가 일치하는 회원을 조회한다") {
            val actual = memberRepository.findByIdOrNull(1L)
            actual.shouldNotBeNull()
        }

        expect("아이디가 일치하는 모든 회원을 조회한다") {
            val actual = memberRepository.findAllByIdIn(listOf(1L, 2L, 3L))
            actual.shouldHaveSize(3)
        }

        expect("이름이나 이메일에 키워드가 포함된 모든 회원을 조회한다") {
            listOf("홍" to 2, "a@" to 1, "" to 3, "4" to 0).forAll { (keyword, size) ->
                val actual = memberRepository.findAllByKeyword(keyword)
                actual shouldHaveSize size
            }
        }

        expect("이메일이 일치하는 회원을 조회한다") {
            val actual = memberRepository.findByEmail("b@email.com")
            actual.shouldNotBeNull()
            actual.name shouldBe "홍길동2"
        }

        expect("이메일이 일치하는 회원이 없으면 null을 반환한다") {
            val actual = memberRepository.findByEmail("notexist@email.com")
            actual.shouldBeNull()
        }

        expect("이메일이 일치하는 모든 회원을 조회한다") {
            val actual = memberRepository.findAllByEmailIn(listOf("b@email.com", "c@email.com"))
            actual shouldHaveSize 2
        }

        expect("이메일이 일치하는 회원이 있는지 확인한다") {
            memberRepository.existsByEmail("a@email.com").shouldBeTrue()
            memberRepository.existsByEmail("non-exists@email.com").shouldBeFalse()
        }
    }

    context("회원 수정") {
        val member = memberRepository.save(
            createMember(
                password = PASSWORD,
                phoneNumber = "010-0000-0000",
            )
        )

        expect("회원이 비밀번호를 수정한다") {
            val actual = memberRepository.getOrThrow(member.id)
            actual.changePassword(PASSWORD, NEW_PASSWORD)
        }

        expect("회원이 휴대전화 번호를 수정한다") {
            val actual = memberRepository.getOrThrow(member.id)
            actual.changePhoneNumber("010-1234-5678")
        }
    }

    context("회원 탈퇴") {
        val member = memberRepository.save(createMember(password = PASSWORD))

        expect("회원 탈퇴하면 회원 정보를 삭제한다") {
            val actual = memberRepository.getOrThrow(member.id)
            actual.withdraw(PASSWORD)
        }
    }

    afterEach {
        entityManager.flush()
        entityManager.clear()
    }

    afterRootTest {
        memberRepository.deleteAll()
    }
})
