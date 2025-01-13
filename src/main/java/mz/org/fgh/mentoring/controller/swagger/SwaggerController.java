package mz.org.fgh.mentoring.controller.swagger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

import java.io.IOException;
import java.io.InputStream;

@Controller("/swagger")
@Secured(SecurityRule.IS_ANONYMOUS)
public class SwaggerController {

    @Get(value = "/swagger.json", produces = MediaType.APPLICATION_JSON)
    public String getSwaggerJson() {
        try (InputStream inputStream = getClass().getResourceAsStream("/META-INF/swagger/mentoring-rest-api-0.1.yml")) {
            ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
            ObjectMapper jsonMapper = new ObjectMapper();
            Object yamlData = yamlMapper.readValue(inputStream, Object.class);
            return jsonMapper.writeValueAsString(yamlData);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Swagger file", e);
        }
    }
}
