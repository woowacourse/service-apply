package apply.domain.member.tobe

import apply.NEW_PASSWORD
import apply.PASSWORD
import apply.domain.member.Password
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.findByIdOrNull
import support.test.RepositoryTest
import support.test.spec.afterRootTest
import java.time.LocalDate

@EnableJpaRepositories("apply.domain.member.tobe")
@EntityScan("apply.domain.member.tobe")
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
            actual.information.shouldNotBeNull()
        }

        expect("아이디가 일치하는 모든 회원을 조회한다") {
            val actual = memberRepository.findAllByIdIn(listOf(1L, 2L, 3L))
            actual.shouldHaveSize(3)
        }

        expect("빈 목록으로 조회하면 빈 목록을 반환한다") {
            val actual = memberRepository.findAllByIdIn(emptyList())
            actual.shouldBeEmpty()
        }
    }

    context("회원 저장") {
        expect("회원과 회원 정보를 함께 저장한다") {
            val member = createMember()
            memberRepository.save(member)
        }
    }

    context("회원 수정") {
        val member = memberRepository.save(createMember())

        expect("회원이 비밀번호를 수정한다") {
            val actual = memberRepository.findByIdOrNull(member.id)!!
            actual.changePassword(PASSWORD, NEW_PASSWORD)
        }

        expect("회원이 휴대전화 번호를 수정한다") {
            val actual = memberRepository.findByIdOrNull(member.id)!!
            actual.changePhoneNumber("010-1234-5678")
        }
    }

    context("회원 탈퇴") {
        val member = memberRepository.save(createMember())

        expect("회원 탈퇴하면 회원 정보를 삭제한다") {
            val actual = memberRepository.findByIdOrNull(member.id)!!
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

private fun createMember(
    email: String = "EMAIL",
    name: String = "NAME",
): Member {
    return Member(
        MemberInformation(email, name, LocalDate.now(), "", ""),
        Password("")
    ) {}
}
