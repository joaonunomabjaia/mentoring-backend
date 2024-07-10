package mz.org.fgh.mentoring.config;
import io.micronaut.context.annotation.ConfigurationProperties;
import jakarta.inject.Singleton;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

@ConfigurationProperties("mentoring")
@Singleton
public class SecretConfig {

    private String dbHostFile;
    private String dbPortFile;
    private String dbUsernameFile;
    private String dbPasswordFile;

    public void setDbHostFile(String dbHostFile) {
        this.dbHostFile = dbHostFile;
    }

    public void setDbPortFile(String dbPortFile) {
        this.dbPortFile = dbPortFile;
    }

    public void setDbUsernameFile(String dbUsernameFile) {
        this.dbUsernameFile = dbUsernameFile;
    }

    public void setDbPasswordFile(String dbPasswordFile) {
        this.dbPasswordFile = dbPasswordFile;
    }

    public String getDbHost() throws IOException {
        return readSecret(dbHostFile);
    }

    public String getDbPort() throws IOException {
        return readSecret(dbPortFile);
    }

    public String getDbUsername() throws IOException {
        return readSecret(dbUsernameFile);
    }

    public String getDbPassword() throws IOException {
        return readSecret(dbPasswordFile);
    }

    private String readSecret(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}