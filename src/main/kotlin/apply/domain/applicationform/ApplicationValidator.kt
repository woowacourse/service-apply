package apply.domain.applicationform

fun interface ApplicationValidator {
    fun validate(memberId: Long, recruitmentId: Long)
}
