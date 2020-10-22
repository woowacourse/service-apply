package apply.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
            .headers().frameOptions().disable()
            .and()
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/admin/**").authenticated()
            .anyRequest().permitAll()
            .and()
            .formLogin().loginPage("/admin/login").permitAll()
            .loginProcessingUrl("/admin/login")
            .defaultSuccessUrl("/admin")
            .failureUrl("/admin/login?error")
            .and().logout().logoutSuccessUrl("/admin/login")
    }
}
