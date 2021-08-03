package apply.domain.applicationform

fun interface ApplicationValidator {
    fun validate(applicantId: Long, recruitmentId: Long)
}
