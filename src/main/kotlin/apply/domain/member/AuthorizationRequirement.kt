package apply.domain.member

fun interface AuthorizationRequirement {
    fun meets(information: MemberInformation): Boolean
}
