package apply.config

import apply.security.AccessorResolver
import apply.security.LoginMemberResolver
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class AuthenticationConfig(
    private val loginMemberResolver: LoginMemberResolver,
    private val accessorResolver: AccessorResolver
) : WebMvcConfigurer {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginMemberResolver)
        resolvers.add(accessorResolver)
    }
}
