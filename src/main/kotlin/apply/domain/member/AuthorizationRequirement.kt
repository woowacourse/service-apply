package apply.domain.member

fun interface AuthorizationRequirement {
    fun require(information: MemberInformation)
}
