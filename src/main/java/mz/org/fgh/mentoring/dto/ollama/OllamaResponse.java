package mz.org.fgh.mentoring.dto.ollama;

public class OllamaResponse {
    private String response;
    private boolean done;

    public String getResponse() {
        return response;
    }

    public boolean isDone() {
        return done;
    }

    // Optional: Add more fields from Ollama's response if needed
}
