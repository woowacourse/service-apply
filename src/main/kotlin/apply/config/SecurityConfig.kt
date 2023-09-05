package apply.config

import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.web.servlet.invoke
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@EnableWebSecurity
class SecurityConfig {
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
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
        return http.build()
    }
}
