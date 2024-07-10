package mz.org.fgh.mentoring.config;


import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;
import java.io.IOException;

@Factory
public class DataSourceFactory {

    private final SecretConfig secretConfig;

    public DataSourceFactory(SecretConfig secretConfig) {
        this.secretConfig = secretConfig;
    }

    @Singleton
    public String dbHost() throws IOException {
        return secretConfig.getDbHost();
    }

    @Singleton
    public String dbPort() throws IOException {
        return secretConfig.getDbPort();
    }

    @Singleton
    public String dbUsername() throws IOException {
        return secretConfig.getDbUsername();
    }

    @Singleton
    public String dbPassword() throws IOException {
        return secretConfig.getDbPassword();
    }
}