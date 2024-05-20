package apply.domain.agreement

import org.springframework.data.jpa.repository.JpaRepository

fun AgreementRepository.getFirstByOrderByVersionDesc(): Agreement = findFirstByOrderByVersionDesc()
    ?: throw NoSuchElementException("동의서가 존재하지 않습니다.")

interface AgreementRepository : JpaRepository<Agreement, Long> {
    fun findFirstByOrderByVersionDesc(): Agreement?
}
