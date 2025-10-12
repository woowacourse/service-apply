package support.infra

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.SOURCE)
annotation class PersistenceOnly(val reason: String = "")
