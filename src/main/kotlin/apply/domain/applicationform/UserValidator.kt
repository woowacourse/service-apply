package apply.domain.applicationform

fun interface UserValidator {
    fun validate(userId: Long, recruitmentId: Long)
}
