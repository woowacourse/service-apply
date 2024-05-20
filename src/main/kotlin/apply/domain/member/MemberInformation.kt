package apply.domain.member

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class MemberInformation(
    @Column(nullable = false, length = 30)
    val name: String,

    @Column(nullable = false)
    val email: String,

    @Column(nullable = false, length = 13)
    val phoneNumber: String,

    @Column(nullable = false)
    val githubUsername: String,

    @Column(nullable = false)
    val birthday: LocalDate,
) {
    fun same(name: String, birthday: LocalDate): Boolean {
        return this.name == name && this.birthday == birthday
    }
}
