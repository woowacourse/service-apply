package apply.domain.member.tobe

import apply.NEW_PASSWORD
import apply.PASSWORD
import apply.domain.member.Password
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.repository.findByIdOrNull
import support.test.RepositoryTest
import java.time.LocalDate

@EnableJpaRepositories("apply.domain.member.tobe")
@EntityScan("apply.domain.member.tobe")
@RepositoryTest
class MemberRepositoryTest(
    private val memberRepository: MemberRepository,
    private val entityManager: TestEntityManager,
) : ExpectSpec({
    extensions(SpringExtension)

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

    afterEach {
        entityManager.flush()
        entityManager.clear()
    }
})

private fun createMember(): Member {
    return Member(
        MemberInformation("", "", LocalDate.now(), "", ""),
        Password("")
    ) {}
}
