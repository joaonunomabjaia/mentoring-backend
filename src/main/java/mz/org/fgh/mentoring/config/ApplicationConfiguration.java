package mz.org.fgh.mentoring.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import lombok.Data;

@Data
@ConfigurationProperties("custom.app")
@Requires(property = "custom.app.env")
public class ApplicationConfiguration {

    private String env;
    @Property(name = "custom.app.base-url")
    private String baseUrl;
}
