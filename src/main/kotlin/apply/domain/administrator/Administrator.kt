package apply.domain.administrator

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Administrator(
    val name: String,

    @Column(unique = true)
    private val username: String,
    private val password: String,
    id: Long = 0L
) : BaseEntity(id), UserDetails {
    override fun getAuthorities(): List<GrantedAuthority> = listOf(SimpleGrantedAuthority("ADMIN"))

    override fun getPassword(): String = password

    override fun getUsername(): String = username

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
