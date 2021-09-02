package apply.domain.cheater

import support.domain.BaseEntity
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
class Cheater(
    @Column(nullable = false)
    val email: String,

    @Column(nullable = false)
    @Lob
    val description: String = "",

    @Column(nullable = false)
    val createdDateTime: LocalDateTime = LocalDateTime.now(),

    id: Long = 0L
) : BaseEntity(id)
