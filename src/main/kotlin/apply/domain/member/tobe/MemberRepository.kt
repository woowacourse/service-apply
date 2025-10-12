package apply.domain.member.tobe

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
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

interface MemberRepository : JpaRepository<Member, Long> {
    @PersistenceOnly(replaceWith = "this.findAllByIdIn(ids)")
    @Query("select m from Member m left join fetch m.information where m.id in :ids")
    override fun findAllById(ids: Iterable<Long>): List<Member>

    @PersistenceOnly(replaceWith = "this.findByEmail(email)")
    @Query("select m from Member m left join fetch m.information where m.information.email = :email")
    fun findByInformationEmail(email: String): Member?

    @PersistenceOnly(replaceWith = "this.findAllByEmailIn(emails)")
    @Query("select m from Member m left join fetch m.information where m.information.email in :emails")
    fun findAllByInformationEmailIn(emails: Collection<String>): List<Member>
}
