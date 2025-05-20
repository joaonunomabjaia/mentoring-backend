package mz.org.fgh.mentoring.service.ai;

import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.ai.client.ollama.OllamaClient;
import mz.org.fgh.mentoring.dto.ollama.OllamaRequest;
import mz.org.fgh.mentoring.dto.ollama.OllamaResponse;

import java.util.List;

@Singleton
public class AiSummaryService {

    private final OllamaClient ollamaClient;

    public AiSummaryService(OllamaClient ollamaClient) {
        this.ollamaClient = ollamaClient;
    }

    public String summarizeWeakPoints(String menteeName, String context) {
        String prompt = "O mentorado " + menteeName + " teve dificuldades nas seguintes áreas:\n" + context +
                "\nGere um resumo amigável e natural em poucas linhas com foco nos pontos fracos para o mentor ler.\n" +
                " Inicia a frase com:  O mentorado " + menteeName + " enfrentou desafios nas seguintes áreas:";

        OllamaRequest request = new OllamaRequest("mistral", prompt, false);

        try {
            OllamaResponse response = ollamaClient.generate(request);
            return response.getResponse() != null ? response.getResponse() : "Não foi possível gerar o resumo.";
        } catch (Exception e) {
            return "Não foi possível gerar o resumo devido a um erro.";
        }
    }

    public String evaluatePerformanceRisk(String menteeName, List<String> sessionSummaries) {
        if (sessionSummaries == null || sessionSummaries.isEmpty()) {
            return "Sem dados suficientes para avaliar o risco de desempenho.";
        }

        StringBuilder context = new StringBuilder();
        for (String summary : sessionSummaries) {
            context.append("- ").append(summary).append("\n");
        }

        String prompt = """
        Abaixo estão os resumos das últimas sessões de mentoria do mentorado %s:

        %s

        Com base nisso, classifique o risco de fraco desempenho contínuo do mentorado, escolhendo uma das seguintes opções:

        🔴 Alto risco — dificuldades recorrentes sem evolução  
        🟡 Médio risco — dificuldades, mas com sinais de melhora  
        🟢 Baixo risco — progresso evidente

        Retorne a classificação com uma justificativa clara em até 3 linhas. Utilize o Português de Portugal nas suas respostas.
        """.formatted(menteeName, context.toString());

        try {
            OllamaRequest request = new OllamaRequest("mistral", prompt, false);
            OllamaResponse response = ollamaClient.generate(request);
            return response.getResponse();
        } catch (Exception e) {
            return "Erro ao avaliar risco de desempenho.";
        }
    }


}