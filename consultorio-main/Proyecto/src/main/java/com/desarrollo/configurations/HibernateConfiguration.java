package com.desarrollo.configurations;

import com.desarrollo.repositories.DesarrolloRepositoryImpl;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author ivanLuna
 */
@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = { "com.desarrollo.entities" })
@EnableJpaRepositories( basePackages = { "com.desarrollo.repositories" }, entityManagerFactoryRef = "entityManager",
        transactionManagerRef = "transaction", repositoryBaseClass = DesarrolloRepositoryImpl.class )
@RequiredArgsConstructor
public class HibernateConfiguration {

    private final DataSource dataSource;

    @PersistenceContext( unitName = "proyecto" )
    @Bean( name = "entityManager" )
    public LocalContainerEntityManagerFactoryBean mysqlEntityManagerFactory ( EntityManagerFactoryBuilder builder ) {
        return builder.dataSource( dataSource )
                    .persistenceUnit( "proyecto" )
                .packages( "com.desarrollo" )
                .build();
    }

    @Bean( name = "transaction")
    public PlatformTransactionManager transactionManager (EntityManagerFactoryBuilder builder) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory( mysqlEntityManagerFactory( builder ).getObject() );
        transactionManager.setDataSource( dataSource );
        return transactionManager;
    }

}
