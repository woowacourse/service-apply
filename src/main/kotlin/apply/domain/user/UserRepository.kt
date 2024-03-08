package apply.domain.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param

fun UserRepository.findByEmail(email: String): Member? = findByInformationEmail(email)
fun UserRepository.findAllByEmailIn(emails: List<String>): List<Member> = findAllByInformationEmailIn(emails)
fun UserRepository.existsByEmail(email: String): Boolean = existsByInformationEmail(email)
fun UserRepository.getOrThrow(id: Long): Member = findByIdOrNull(id)
    ?: throw NoSuchElementException("회원이 존재하지 않습니다. id: $id")

interface UserRepository : JpaRepository<Member, Long> {
    @Query("select a from Member a where a.information.name like %:keyword% or a.information.email like %:keyword%")
    fun findAllByKeyword(@Param("keyword") keyword: String): List<Member>
    fun findByInformationEmail(email: String): Member?
    fun findAllByInformationEmailIn(emails: List<String>): List<Member>
    fun existsByInformationEmail(email: String): Boolean
}
