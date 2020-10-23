package apply.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.web.servlet.invoke

@EnableWebSecurity
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http {
            headers {
                frameOptions { disable() }
            }
            csrf { disable() }
            authorizeRequests {
                authorize("/admin/**", authenticated)
                authorize("/**", permitAll)
            }
            formLogin {
                loginPage = "/admin/login"
                permitAll = true
                loginProcessingUrl = "/admin/login"
                failureUrl = "/admin/login?error"
                defaultSuccessUrl("/admin", false)
            }
            logout {
                logoutSuccessUrl = "/admin/login"
            }
        }
    }
}
