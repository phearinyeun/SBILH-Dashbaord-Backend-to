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
@EnableJpaRepositories(entityManagerFactoryRef = "secondaryEntityManagerFactory", basePackages = {
        "com.sbilhbank.insur.repository.secondary" })
public class SecondaryDataSourceConfig {
    @Autowired
    private Environment env;
    @Bean(name = "dataSecondarySource")
    @ConfigurationProperties(prefix = "spring.secondary.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "secondaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder,
                                                                       @Qualifier("dataSecondarySource") DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();


        em.setDataSource(dataSource);
        em.setPackagesToScan("com.sbilhbank.insur.entity.secondary");
        em.setPersistenceUnitName("PERSITENCE_UNIT_NAME_2"); // Important !!
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", env.getProperty("spring.secondary.jpa.properties.hibernate.dialect"));
        properties.put("hibernate.ddl-auto",  env.getProperty("spring.secondary.jpa.hibernate.ddl-auto"));
        properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.secondary.jpa.hibernate.hbm2ddl-auto"));
        properties.put("hibernate.show_sql", env.getProperty("spring.secondary.jpa.hibernate.show_sql"));
        properties.put("hibernate.physical_naming_strategy", CamelCaseToUnderscoresNamingStrategy.class.getName());
        properties.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class.getName());
        properties.put("hibernate.jdbc.lob.non_contextual_creation", env.getProperty("spring.secondary.jpa.properties.hibernate.jdbc.lob.non_contextual_creation"));
        em.setJpaPropertyMap(properties);
        em.afterPropertiesSet();
        return em;
        //        return builder.dataSource(dataSource).packages("com.sbilhbank.insur.entity.secondary").persistenceUnit("secondary")
//               .build();
    }
    @Bean
    public PlatformTransactionManager transactionSecondaryManager(
            @Qualifier("secondaryEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);

    }
}