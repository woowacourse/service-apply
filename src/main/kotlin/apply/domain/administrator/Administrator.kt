package apply.domain.administrator

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import support.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity

@Entity
class Administrator(
    @Column(unique = true)
    private val name: String,
    private val password: String,
    id: Long
) : BaseEntity(id), UserDetails {
    override fun getAuthorities(): List<GrantedAuthority> = listOf(SimpleGrantedAuthority("ADMIN"))

    override fun getPassword(): String = password

    override fun getUsername(): String = name

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
