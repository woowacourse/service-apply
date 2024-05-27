package apply.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param

fun MemberRepository.findAllByIdIn(ids: Collection<Long>): List<Member> {
    if (ids.isEmpty()) return emptyList()
    return findAllById(ids)
}

fun MemberRepository.findByEmail(email: String): Member? = findBy_informationEmail(email)
fun MemberRepository.findAllByEmailIn(emails: List<String>): List<Member> {
    if (emails.isEmpty()) return emptyList()
    return findAllBy_informationEmailIn(emails)
}

fun MemberRepository.existsByEmail(email: String): Boolean = existsBy_informationEmail(email)
fun MemberRepository.getOrThrow(id: Long): Member = findByIdOrNull(id)
    ?: throw NoSuchElementException("회원이 존재하지 않습니다. id: $id")

interface MemberRepository : JpaRepository<Member, Long> {
    @Query("select m from Member m join fetch m._information where m.id in :ids")
    override fun findAllById(@Param("ids") ids: Iterable<Long>): List<Member>

    @Query("select m from Member m join fetch m._information where m._information.name like %:keyword% or m._information.email like %:keyword%")
    fun findAllByKeyword(@Param("keyword") keyword: String): List<Member>

    @Query("select m from Member m join fetch m._information where m._information.email = :email")
    fun findBy_informationEmail(@Param("email") email: String): Member?

    @Query("select m from Member m join fetch m._information where m._information.email in :emails")
    fun findAllBy_informationEmailIn(@Param("emails") emails: List<String>): List<Member>
    fun existsBy_informationEmail(email: String): Boolean
}
