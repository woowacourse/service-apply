package apply.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@ConfigurationProperties("database.initialization")
@ConstructorBinding
data class DatabaseInitializationProperties(val excludedTableNames: List<String>)

interface Database {
    fun retrieveTables(): List<String>
    fun clear(tableNames: List<String>)
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

    @Transactional
    override fun clear(tableNames: List<String>) {
        entityManager.createNativeQuery(constraintsOffSql).executeUpdate()
        tableNames
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

@Profile("local")
@Component
class MySql(
    entityManager: EntityManager,
    properties: DatabaseInitializationProperties
) : AbstractDatabase(entityManager, properties) {
    override val metaTablesSql: String = "show tables"
    override val constraintsOffSql: String = "set foreign_key_checks = 0"
    override val constraintsOnSql: String = "set foreign_key_checks = 1"
    override fun createTruncateTableSql(tableName: String): String = "truncate table $tableName"
}

@Profile("test")
@Component
class H2(
    entityManager: EntityManager,
    properties: DatabaseInitializationProperties
) : AbstractDatabase(entityManager, properties) {
    override val metaTablesSql: String = "show tables"
    override val constraintsOffSql: String = "set referential_integrity false"
    override val constraintsOnSql: String = "set referential_integrity true"
    override fun createTruncateTableSql(tableName: String): String = "truncate table $tableName restart identity"
}
