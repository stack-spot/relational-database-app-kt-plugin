{%-
    set driverDict = {
        'PostgreSQL': 'org.postgresql:postgresql',
        'MySQL': 'mysql:mysql-connector-java',
        'MariaDB': 'org.mariadb.jdbc:mariadb-java-client'
    }
-%}
{%-
    set migrationDict = {
        'Flyway': 'org.flywaydb:flyway-core',
        'Liquibase': 'org.liquibase:liquibase-core'
    }
-%}

plugins {
    kotlin("plugin.jpa") version "1.6.10"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly(kotlin("reflect"))
    runtimeOnly("{{driverDict[inputs.dbms]}}")
    {% if inputs.migration_tool in migrationDict  %}
    runtimeOnly("{{migrationDict[inputs.migration_tool]}}")
    {% endif %}
}
