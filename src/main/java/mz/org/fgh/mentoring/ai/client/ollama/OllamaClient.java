package mz.org.fgh.mentoring.ai.client.ollama;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.client.annotation.Client;
import mz.org.fgh.mentoring.dto.ollama.OllamaRequest;
import mz.org.fgh.mentoring.dto.ollama.OllamaResponse;

@Client("ollama")
@Produces("application/json")
public interface OllamaClient {

    @Post("/api/generate")
    OllamaResponse generate(@Body @javax.validation.constraints.NotNull OllamaRequest request);


}
