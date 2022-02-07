package {{project_group_id}}.config

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.jdbc.datasource.DriverManagerDataSource
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(
    basePackages = ["{{project_group_id}}.{{repository_path}}"],
    entityManagerFactoryRef = "{{inputs.datasource_name|to_lower_camel}}EntityManager",
    transactionManagerRef = "{{inputs.datasource_name|to_lower_camel}}TransactionManager"
)
class {{inputs.datasource_name|to_camel}}DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "{{inputs.datasource_name|to_lower_camel}}.datasource")
    fun {{inputs.datasource_name|to_lower_camel}}DataSource(): DataSource = DriverManagerDataSource()

    @Bean
    fun {{inputs.datasource_name|to_lower_camel}}EntityManager(@Qualifier("{{inputs.datasource_name|to_lower_camel}}DataSource") {{inputs.datasource_name|to_lower_camel}}DataSource: DataSource): LocalContainerEntityManagerFactoryBean =
        (LocalContainerEntityManagerFactoryBean()).apply {
            dataSource = {{inputs.datasource_name|to_lower_camel}}DataSource
            setPackagesToScan("{{project_group_id}}.{{repository_path}}")
            jpaVendorAdapter = HibernateJpaVendorAdapter()
        }

    @Bean
    fun {{inputs.datasource_name|to_lower_camel}}TransactionManager(@Qualifier("{{inputs.datasource_name|to_lower_camel}}EntityManager") entityManagerFactory: LocalContainerEntityManagerFactoryBean) =
        JpaTransactionManager(entityManagerFactory.`object`!!)
}
