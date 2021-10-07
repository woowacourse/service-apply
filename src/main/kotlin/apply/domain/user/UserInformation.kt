package apply.domain.user

import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
class UserInformation(
    @Column(nullable = false)
    val name: String,

    @Column(unique = true, nullable = false)
    val email: String,
    phoneNumber: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Column(nullable = false)
    val birthday: LocalDate
) {
    @Column(nullable = false)
    var phoneNumber = phoneNumber
        private set

    fun changePhoneNumber(phoneNumber: String) {
        this.phoneNumber = phoneNumber
    }
    fun same(name: String, birthday: LocalDate): Boolean {
        return this.name == name && this.birthday == birthday
    }

    // TODO: 회원가입, 로그인 로직이 분리되면 authenticate 에서 비교가 사라지므로 equals, hashcode 삭제가능
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserInformation

        if (name != other.name) return false
        if (email != other.email) return false
        if (gender != other.gender) return false
        if (birthday != other.birthday) return false
        if (phoneNumber != other.phoneNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + gender.hashCode()
        result = 31 * result + birthday.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        return result
    }
}
