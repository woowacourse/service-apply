package apply.domain.judgment

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Commit(
    @Column(name = "commit_hash", nullable = false, columnDefinition = "char(40)")
    val hash: String
)
