package support.infra

@Target(AnnotationTarget.PROPERTY, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class PersistenceOnly(val reason: String = "", val replaceWith: String = "")
