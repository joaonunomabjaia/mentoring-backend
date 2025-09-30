package mz.org.fgh.mentoring.service.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.entity.earesource.Resource;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.repository.resource.ResourceRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;

import java.util.*;

@Singleton
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final SettingsRepository settingsRepository;

    public ResourceService(ResourceRepository resourceRepository, SettingsRepository settingsRepository) {
        this.resourceRepository = resourceRepository;
        this.settingsRepository = settingsRepository;
    }

    public List<Map<String, String>> extractResourceSummariesByProgram(String program) {
        List<Map<String, String>> summaries = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        Optional<Resource> resourceJson = resourceRepository.findAll().stream().findFirst();
        if (resourceJson.isEmpty()) return summaries;

        String baseUrl = settingsRepository.findByDesignation("SERVER_URL")
                .map(Setting::getValue)
                .orElse("http://localhost:8086");

        try {
            JsonNode root = objectMapper.readTree(resourceJson.get().getResource());
            extractResourcesRecursivelyByProgram(root, summaries, baseUrl, program, program);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return summaries;
    }

    private void extractResourcesRecursivelyByProgram(JsonNode node, List<Map<String, String>> summaries, String baseUrl, String targetProgram, String currentProgram) {
        if (node.isArray()) {
            for (JsonNode element : node) {
                extractResourcesRecursivelyByProgram(element, summaries, baseUrl, targetProgram, currentProgram);
            }
        } else if (node.isObject()) {
            // Atualiza o programa atual se estiver no nível do programa
            if (node.has("label") && node.has("clickable") && node.get("clickable").asInt() == 0 && !node.has("type")) {
                currentProgram = node.get("label").asText();
            }

            // Se for um recurso (clickable == 2) e o programa atual for o procurado
            if (node.has("clickable") && node.get("clickable").asInt() == 2 && targetProgram.equalsIgnoreCase(currentProgram)) {
                Map<String, String> resource = new HashMap<>();
                String name = node.has("name") ? node.get("name").asText() : "";
                String description = node.has("description") ? node.get("description").asText() : "";

                resource.put("name", name);
                resource.put("description", description);
                resource.put("link", baseUrl + "/api/resource/load?fileName=" + name);

                summaries.add(resource);
            }

            // Continua a recursão nos filhos
            if (node.has("children")) {
                extractResourcesRecursivelyByProgram(node.get("children"), summaries, baseUrl, targetProgram, currentProgram);
            }
        }
    }


}
