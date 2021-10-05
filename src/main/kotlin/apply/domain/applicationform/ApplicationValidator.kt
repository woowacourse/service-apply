package apply.domain.applicationform

fun interface ApplicationValidator {
    fun validate(userId: Long, recruitmentId: Long)
}
