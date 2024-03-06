package apply.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param

fun MemberRepository.findByEmail(email: String): Member? = findByInformationEmail(email)
fun MemberRepository.findAllByEmailIn(emails: List<String>): List<Member> = findAllByInformationEmailIn(emails)
fun MemberRepository.existsByEmail(email: String): Boolean = existsByInformationEmail(email)
fun MemberRepository.getOrThrow(id: Long): Member = findByIdOrNull(id)
    ?: throw NoSuchElementException("회원이 존재하지 않습니다. id: $id")

interface MemberRepository : JpaRepository<Member, Long> {
    @Query("select m from Member m where m.information.name like %:keyword% or m.information.email like %:keyword%")
    fun findAllByKeyword(@Param("keyword") keyword: String): List<Member>
    fun findByInformationEmail(email: String): Member?
    fun findAllByInformationEmailIn(emails: List<String>): List<Member>
    fun existsByInformationEmail(email: String): Boolean
}
