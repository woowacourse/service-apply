package apply.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import support.infra.PersistenceOnly

interface MemberRepository : JpaRepository<Member, Long> {
    fun getOrThrow(id: Long): Member = findByIdOrNull(id)
        ?: throw NoSuchElementException("회원이 존재하지 않습니다. id: $id")

    fun findAllByIdInAndKeyword(ids: Collection<Long>, keyword: String?): List<Member> {
        return if (keyword.isNullOrEmpty()) {
            findAllByIdIn(ids)
        } else {
            findAllByKeyword(keyword).filter { it.id in ids }
        }
    }

    fun findAllByIdIn(ids: Collection<Long>): List<Member> {
        if (ids.isEmpty()) return emptyList()
        return findAllById(ids)
    }

    fun findAllActiveByIdIn(ids: Collection<Long>): List<Member> {
        if (ids.isEmpty()) return emptyList()
        return findAllByIdAndStatus(ids, MemberStatus.ACTIVE)
    }

    fun findByEmail(email: String): Member? = findBy_informationEmail(email)
    fun findAllByEmailIn(emails: Collection<String>): List<Member> {
        if (emails.isEmpty()) return emptyList()
        return findAllBy_informationEmailIn(emails)
    }

    fun existsByEmail(email: String): Boolean = existsBy_informationEmail(email)

    @Query(
        """
        select m
        from Member m
        left join fetch m._information
        where m.status = 'ACTIVE'
            and (
                m._information.name like %:keyword%
                or m._information.email like %:keyword%
            )
        """
    )
    fun findAllByKeyword(keyword: String): List<Member>

    @PersistenceOnly(replaceWith = "this.findAllByIdIn(ids)")
    @Query(
        """
        select m
        from Member m
        left join fetch m._information
        where m.id in :ids
        """
    )
    override fun findAllById(ids: Iterable<Long>): List<Member>

    @PersistenceOnly(replaceWith = "this.findAllActiveByIdIn(ids)")
    @Query(
        """
        select m
        from Member m
        left join fetch m._information
        where m.status = :status and m.id in :ids
        """
    )
    fun findAllByIdAndStatus(ids: Collection<Long>, status: MemberStatus): List<Member>

    @PersistenceOnly(replaceWith = "this.findByEmail(email)")
    @Query("select m from Member m left join fetch m._information where m._information.email = :email")
    fun findBy_informationEmail(email: String): Member?

    @PersistenceOnly(replaceWith = "this.findAllByEmailIn(emails)")
    @Query("select m from Member m left join fetch m._information where m._information.email in :emails")
    fun findAllBy_informationEmailIn(emails: Collection<String>): List<Member>

    @PersistenceOnly(replaceWith = "this.existsByEmail(email)")
    fun existsBy_informationEmail(email: String): Boolean
}
