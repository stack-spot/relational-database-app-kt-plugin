# relational-database-app-kt-plugin

Configures JDBC connection and Spring Data JPA for a supported DBMS, which is inferred from the provided JDBC connection string.
Also allows setup of a supported migration tool.

### Supported DBMS:
- MariaDB
- MySQL
- PostgreSQL

### Supported migration tools:
- Flyway
- Liquibase

### CDK:
This plugin also provide a CDK code to provision a RDS database instance with a basic configuration according to the chosen DBMS.
