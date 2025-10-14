package apply.domain.member.tobe

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import support.infra.PersistenceOnly

fun MemberRepository.findAllByIdIn(ids: Collection<Long>): List<Member> {
    if (ids.isEmpty()) return emptyList()
    return findAllById(ids)
}

fun MemberRepository.findByEmail(email: String): Member? = findByInformationEmail(email)
fun MemberRepository.findAllByEmailIn(emails: Collection<String>): List<Member> {
    if (emails.isEmpty()) return emptyList()
    return findAllByInformationEmailIn(emails)
}

fun MemberRepository.existsByEmail(email: String): Boolean = existsBy_informationEmail(email)
fun MemberRepository.getOrThrow(id: Long): Member = findByIdOrNull(id)
    ?: throw NoSuchElementException("회원이 존재하지 않습니다. id: $id")

interface MemberRepository : JpaRepository<Member, Long> {
    @PersistenceOnly(replaceWith = "this.findAllByIdIn(ids)")
    @Query("select m from Member m left join fetch m._information where m.id in :ids")
    override fun findAllById(ids: Iterable<Long>): List<Member>

    @PersistenceOnly(replaceWith = "this.findByEmail(email)")
    @Query("select m from Member m left join fetch m._information where m._information.email = :email")
    fun findByInformationEmail(email: String): Member?

    @PersistenceOnly(replaceWith = "this.findAllByEmailIn(emails)")
    @Query("select m from Member m left join fetch m._information where m._information.email in :emails")
    fun findAllByInformationEmailIn(emails: Collection<String>): List<Member>

    @PersistenceOnly(replaceWith = "this.existsByEmail(email)")
    fun existsBy_informationEmail(email: String): Boolean

    @Query("select m from Member m left join fetch m._information where m._information.name like %:keyword% or m._information.email like %:keyword%")
    fun findAllByKeyword(keyword: String): List<Member>
}
