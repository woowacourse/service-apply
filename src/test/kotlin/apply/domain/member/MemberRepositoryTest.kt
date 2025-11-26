package apply.domain.member

import apply.NEW_PASSWORD
import apply.PASSWORD
import apply.createMember
import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.inspectors.forAll
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.longs.shouldNotBeZero
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import support.test.RepositoryTest
import support.test.autoconfigure.orm.jpa.flushAndClear
import support.test.spec.afterRootTest

@RepositoryTest
class MemberRepositoryTest(
    private val memberRepository: MemberRepository,
    private val entityManager: TestEntityManager,
) : ExpectSpec({
    extensions(SpringExtension)

    context("회원 저장") {
        expect("회원과 회원 정보를 함께 저장한다") {
            val actual = memberRepository.save(createMember())
            actual.id.shouldNotBeZero()
            shouldNotThrowAny { actual.information }
        }
    }

    context("회원 수정") {
        val base = memberRepository.save(createMember(password = PASSWORD, phoneNumber = "+821012345678"))

        expect("회원이 비밀번호를 초기화한다") {
            val member = memberRepository.getOrThrow(base.id)
            member.resetPassword(base.name, base.birthday, "new_password")
            val actual = memberRepository.save(member)
            actual.id.shouldNotBeZero()
            actual.password shouldBe Password("new_password")
            shouldNotThrowAny { actual.information }
        }

        expect("회원이 비밀번호를 수정한다") {
            val member = memberRepository.getOrThrow(base.id)
            member.changePassword(PASSWORD, NEW_PASSWORD)
            entityManager.flushAndClear()
            val actual = memberRepository.getOrThrow(member.id)
            actual.password shouldBe NEW_PASSWORD
        }

        expect("회원이 휴대전화 번호를 수정한다") {
            val member = memberRepository.getOrThrow(base.id)
            member.changePhoneNumber("+821099999999")
            entityManager.flushAndClear()
            val actual = memberRepository.getOrThrow(member.id)
            actual.information.phoneNumber shouldBe "+821099999999"
        }
    }

    context("회원 탈퇴") {
        val base = memberRepository.save(createMember())

        expect("회원 탈퇴하면 회원 정보를 삭제한다") {
            val member = memberRepository.getOrThrow(base.id)
            member.withdraw(PASSWORD)
            entityManager.flushAndClear()
            val actual = memberRepository.getOrThrow(member.id)
            actual.status shouldBe MemberStatus.WITHDRAWN
            shouldThrow<IllegalStateException> { actual.information }
        }
    }

    context("회원 조회") {
        val members = memberRepository.saveAll(
            listOf(
                createMember(name = "홍길동1", email = "a@email.com"),
                createMember(name = "홍길동2", email = "b@email.com"),
                createMember(name = "동해물과백두산이마르고닳도록하느님이보우하사우리나라만세무궁", email = "c@email.com"),
            )
        )

        expect("아이디가 일치하는 회원을 조회한다") {
            val actual = memberRepository.getOrThrow(members[0].id)
            actual.information.shouldNotBeNull()
        }

        expect("아이디가 일치하는 모든 회원을 조회한다") {
            val actual = memberRepository.findAllByIdIn(members.map(Member::id))
            actual.shouldHaveSize(3)
        }

        expect("이메일이 일치하는 회원을 조회한다") {
            val actual = memberRepository.findByEmail("b@email.com")
            actual.shouldNotBeNull()
            actual.information.shouldNotBeNull()
            actual.information.name shouldBe "홍길동2"
        }

        expect("이메일이 일치하는 회원이 없으면 null을 반환한다") {
            val actual = memberRepository.findByEmail("notexist@email.com")
            actual.shouldBeNull()
        }

        expect("이메일이 일치하는 모든 회원을 조회한다") {
            val actual = memberRepository.findAllByEmailIn(listOf("b@email.com", "c@email.com"))
            actual shouldHaveSize 2
        }

        expect("빈 목록으로 조회하면 빈 목록을 반환한다") {
            memberRepository.findAllByIdIn(emptyList()).shouldBeEmpty()
            memberRepository.findAllByEmailIn(emptyList()).shouldBeEmpty()
        }

        expect("이메일이 일치하는 회원이 있는지 확인한다") {
            memberRepository.existsByEmail("a@email.com").shouldBeTrue()
            memberRepository.existsByEmail("non-exists@email.com").shouldBeFalse()
        }

        expect("이름이나 이메일에 키워드가 포함된 모든 회원을 조회한다") {
            listOf("홍" to 2, "a@" to 1, "" to 3, "4" to 0).forAll { (keyword, size) ->
                val actual = memberRepository.findAllByKeyword(keyword)
                actual shouldHaveSize size
            }
        }
    }

    afterRootTest {
        memberRepository.deleteAll()
    }
})
