package apply.domain.term

import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Term(
    @Column(nullable = false)
    val name: String,
    id: Long = 0L
) : BaseEntity(id)
