package com.sbilhbank.insur.config.db;


import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "primaryEntityManagerFactory", basePackages = {
        "com.sbilhbank.insur.repository.primary" })
public class PrimaryDataSourceConfig {
    @Autowired
    private Environment env;
    @Primary
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dataSource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();

        em.setDataSource(dataSource);
        em.setPackagesToScan("com.sbilhbank.insur.entity.primary");
        em.setPersistenceUnitName("PERSITENCE_UNIT_NAME_1"); // Important !!
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.ddl-auto",  env.getProperty("spring.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.hbm2ddl-auto"));
        properties.put("hibernate.show_sql", env.getProperty("spring.jpa.hibernate.show_sql"));
        properties.put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy.class.getName());
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        properties.put("hibernate.jdbc.lob.non_contextual_creation", env.getProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation"));
        em.setJpaPropertyMap(properties);
        em.afterPropertiesSet();
        return em;
//        return builder.dataSource(dataSource).packages("com.sbilhbank.insur.entity.primary").persistenceUnit("primary")
//                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager transactionManager(
            @Qualifier("primaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);

    }
}
