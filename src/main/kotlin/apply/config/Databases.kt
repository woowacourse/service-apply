package apply.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.stereotype.Component
import javax.persistence.EntityManager

@ConfigurationProperties("database.initialization")
@ConstructorBinding
data class DatabaseInitializationProperties(val excludedTableNames: List<String>)

interface Database {
    fun retrieveTables(): List<String>
    fun clear(vararg tableNames: String)
}

abstract class AbstractDatabase(
    private val entityManager: EntityManager,
    private val properties: DatabaseInitializationProperties
) : Database {
    override fun retrieveTables(): List<String> {
        return entityManager.createNativeQuery(metaTablesSql).resultList
            .map { it.toString() }
            .excluded()
    }

    override fun clear(vararg tableNames: String) {
        entityManager.createNativeQuery(constraintsOffSql).executeUpdate()
        tableNames.toList()
            .excluded()
            .forEach { entityManager.createNativeQuery(createTruncateTableSql(it)).executeUpdate() }
        entityManager.createNativeQuery(constraintsOnSql).executeUpdate()
    }

    private fun List<String>.excluded(): List<String> {
        return filterNot { it in properties.excludedTableNames }
    }

    abstract val metaTablesSql: String
    abstract val constraintsOffSql: String
    abstract val constraintsOnSql: String
    abstract fun createTruncateTableSql(tableName: String): String
}

@Component
class MySql(
    entityManager: EntityManager,
    properties: DatabaseInitializationProperties
) : AbstractDatabase(entityManager, properties) {
    override val metaTablesSql: String = "SHOW TABLES"
    override val constraintsOffSql: String = "SET FOREIGN_KEY_CHECKS = 0"
    override val constraintsOnSql: String = "SET FOREIGN_KEY_CHECKS = 1"
    override fun createTruncateTableSql(tableName: String): String = "TRUNCATE TABLE $tableName"
}

@Component
class H2(
    entityManager: EntityManager,
    properties: DatabaseInitializationProperties
) : AbstractDatabase(entityManager, properties) {
    override val metaTablesSql: String = "SHOW TABLES"
    override val constraintsOffSql: String = "SET REFERENTIAL_INTEGRITY FALSE"
    override val constraintsOnSql: String = "SET REFERENTIAL_INTEGRITY TRUE"
    override fun createTruncateTableSql(tableName: String): String = "TRUNCATE TABLE $tableName RESTART IDENTITY"
}
