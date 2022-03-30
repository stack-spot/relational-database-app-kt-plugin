# Relational Database Plugin

- **Descrição:** O plugin [`relational-database-app-kt-plugin`](https://github.com/stack-spot/relational-database-app-kt-plugin.git) configura a conexão de uma aplicação a um banco de dados relacional. É possível configurar também uma ferramenta de migração de banco de dados.
- **Categoria:** Database.
- **Stack:** zup-kotlin-stack
- **Criado em:** 16/12/21
- **Última atualização:** 28/03/22
- **Download:** https://github.com/stack-spot/relational-database-app-kt-plugin.git

## **Visão Geral**
### **relational-database-app-kt-plugin**

O **relational-database-app-kt-plugin** é um plugin que visa configurar a conexão JDBC e Spring Data JPA de uma aplicação a um banco de dados relacional, dentre os suportados: [`MariaDb`](https://mariadb.org/), [`Mysql`](https://www.mysql.com/) e [`PostgreSQL`](https://www.postgresql.org/)
Com o plugin, também é possível configurar, de forma opcional, uma ferramenta de migração, dentre as suportadas: [`Flyway`](https://flywaydb.org/) e [`Liquibase`](https://liquibase.org/). Para o provisionamento de uma instância RDS na AWS, o plugin provê código CDK com uma configuração básica de acordo com o sistema gerenciador de banco de dados escolhido.

## **Uso**

### **Pré-requisitos**
Para utilizar esse plugin, é necessário ter uma stack Spring Kotlin criada através da Stack Spot CLI.

### **Aplicação**
Após gerar um projeto utilizando o **spring-app-kt-template**, acesse o diretório do projeto e aplique o **relational-database-app-kt-plugin** através do comando:

```
stk apply plugin zup-kotlin-stack/relational-database-app-kt-plugin
```

## **Configuração**

### **Inputs**
Os inputs necessários para utilizar o plugin são:

| **Campo** | **Valor** | **Descrição** |
| :--- | :--- | :--- |
| Which database would you like to use? | MariaDB/MySQL/PostgreSQL | Escolha qual banco de dados relacional deseja utilizar |
| Which migration tool would you like to use with this datasource? | Flyway/Liquibase/None | Escolha qual ferramenta de migração deseja utilizar |
