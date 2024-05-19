package apply.domain.agreement

import support.domain.BaseRootEntity
import java.time.format.DateTimeFormatter
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Agreement(
    @Column(nullable = false)
    val version: Int,

    @Column(nullable = false, length = 5000)
    val content: String,
    id: Long = 0L,
) : BaseRootEntity<Agreement>(id) {
    init {
        runCatching { ISO_BASIC.parse(version.toString()) }
            .also { require(it.isSuccess) { "버전 형식이 일치하지 않습니다." } }
    }

    companion object {
        private val ISO_BASIC: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    }
}
