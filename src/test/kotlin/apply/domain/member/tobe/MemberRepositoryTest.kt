package apply.domain.member.tobe

import apply.domain.member.Password
import io.kotest.core.spec.style.ExpectSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
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
            val member = Member(
                MemberInformation("", "", LocalDate.now(), "", ""),
                Password("")
            ) {}
            memberRepository.save(member)
            entityManager.flush()
        }
    }
})
