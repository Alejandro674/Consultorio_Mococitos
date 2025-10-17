package com.desarrollo.configurations;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author ivanLuna
 */
@Configuration
@RequiredArgsConstructor
public class DataSourceConfiguration {

    private final ProyectoConfiguration proyectoConfiguration;

    @Bean
    public DataSource dataSource () {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setJdbcUrl( proyectoConfiguration.getDatabaseUrl().concat( proyectoConfiguration.getDatabaseName() ) );
        dataSource.setPassword( proyectoConfiguration.getDatabasePassword() );
        dataSource.setUser( proyectoConfiguration.getDatabaseUser() );
        dataSource.setTestConnectionOnCheckin( true );
        try {
            dataSource.setDriverClass( "org.mariadb.jdbc.Driver");
        } catch ( Exception e ) {
            throw new RuntimeException( "Error al cargar el driver de la base de datos." );
        }
        return dataSource;
    }

}
