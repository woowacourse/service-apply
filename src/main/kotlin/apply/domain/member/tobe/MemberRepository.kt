package apply.domain.member.tobe

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import support.infra.PersistenceOnly

fun MemberRepository.findAllByIdIn(ids: Collection<Long>): List<Member> {
    if (ids.isEmpty()) return emptyList()
    return findAllById(ids)
}

interface MemberRepository : JpaRepository<Member, Long> {
    @PersistenceOnly(replaceWith = "this.findAllByIdIn(ids)")
    @Query("select m from Member m left join fetch m.information where m.id in :ids")
    override fun findAllById(ids: Iterable<Long>): List<Member>
}
