package com.desarrollo.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;

/**
 * @author ivanLuna
 */
@Configuration
@PropertySource( value = "classpath:application.yaml")
@Primary
public class ProyectoConfiguration {

    @Value("${app.database.url}")
    private String databaseUrl;
    @Value("${app.database.database}")
    private String databaseName;
    @Value("${app.database.user}")
    private String databaseUser;
    @Value("${app.database.password}")
    private String databasePassword;

    public String getDatabaseUrl() {
        return this.databaseUrl;
    }
    public String getDatabaseName() {
        return this.databaseName;
    }
    public String getDatabaseUser() { return this.databaseUser; }
    public String getDatabasePassword() { return this.databasePassword; }

}
