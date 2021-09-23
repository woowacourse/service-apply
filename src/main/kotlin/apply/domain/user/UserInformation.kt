package apply.domain.user

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class UserInformation(
    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val phoneNumber: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Column(nullable = false)
    val birthday: LocalDate
) {
    fun same(name: String, birthday: LocalDate): Boolean {
        return this.name == name && this.birthday == birthday
    }
}
