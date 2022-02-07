{%-
    set driverDict = {
        'postgresql': 'org.postgresql:postgresql',
        'mysql': 'mysql:mysql-connector-java',
        'mariadb': 'org.mariadb.jdbc:mariadb-java-client',
        'sqlserver': 'com.microsoft.sqlserver:mssql-jdbc',
        'oracle': 'com.oracle.database.jdbc:ojdbc8'
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
    {% if computed_inputs.dbms in driverDict %}
    runtimeOnly("{{driverDict[computed_inputs.dbms]}}")
    {% endif %}
    {% if inputs.migration_tool in migrationDict  %}
    runtimeOnly("{{migrationDict[inputs.migration_tool]}}")
    {% endif %}
}
