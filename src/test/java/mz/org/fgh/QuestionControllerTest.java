package mz.org.fgh;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.service.question.QuestionService;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@MicronautTest
class QuestionControllerTest {

    @Inject
    QuestionService questionService;

    @Inject
    @Client("/")
    HttpClient client;

    @Test
    void testGetAllQuestions() {
        when(questionService.getAllQuestions()).thenReturn(Collections.emptyList());

        HttpRequest<?> request = HttpRequest.GET(RESTAPIMapping.QUESTION + "/getAll");
        HttpResponse<List<QuestionDTO>> response = client.toBlocking().exchange(request, Argument.listOf(QuestionDTO.class));

        assertEquals(200, response.getStatus().getCode());
        assertEquals(0, response.body().size());
    }

    @Test
    void testFindQuestionById() {
        Question question = new Question();
        question.setId(1L);
        when(questionService.findById(1L)).thenReturn(Optional.of(question));

        HttpRequest<?> request = HttpRequest.GET(RESTAPIMapping.QUESTION + "/1");
        HttpResponse<QuestionDTO> response = client.toBlocking().exchange(request, QuestionDTO.class);

        assertEquals(200, response.getStatus().getCode());
        assertEquals(1L, response.body().getId());
    }

    @Test
    void testCreateQuestion() {
        Question question = new Question();
        question.setId(1L);
        when(questionService.create(any(Question.class), any(Long.class))).thenReturn(question);

        QuestionDTO questionDTO = new QuestionDTO();
        HttpRequest<?> request = HttpRequest.POST(RESTAPIMapping.QUESTION + "/save", questionDTO);
        HttpResponse<QuestionDTO> response = client.toBlocking().exchange(request, QuestionDTO.class);

        assertEquals(200, response.getStatus().getCode());
        assertEquals(1L, response.body().getId());
    }

    @Test
    void testUpdateQuestion() {
        Question question = new Question();
        question.setId(1L);
        when(questionService.findById(1L)).thenReturn(Optional.of(question));
        when(questionService.update(any(Question.class), any(Long.class))).thenReturn(question);

        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        HttpRequest<?> request = HttpRequest.PATCH(RESTAPIMapping.QUESTION + "/update", questionDTO);
        HttpResponse<QuestionDTO> response = client.toBlocking().exchange(request, QuestionDTO.class);

        assertEquals(200, response.getStatus().getCode());
        assertEquals(1L, response.body().getId());
    }

    @Test
    void testDeleteQuestion() {
        Question question = new Question();
        question.setId(1L);
        when(questionService.findById(1L)).thenReturn(Optional.of(question));
        when(questionService.delete(any(Question.class), any(Long.class))).thenReturn(question);

        QuestionDTO questionDTO = new QuestionDTO();
        HttpRequest<?> request = HttpRequest.PATCH(RESTAPIMapping.QUESTION + "/update", questionDTO);
        HttpResponse<QuestionDTO> response = client.toBlocking().exchange(request, QuestionDTO.class);

        assertEquals(200, response.getStatus().getCode());
        assertEquals(1L, response.body().getId());
    }

    @Test
    void testDestroyQuestion() {
        Question question = new Question();
        question.setId(1L);
        when(questionService.findById(1L)).thenReturn(Optional.of(question));

        HttpRequest<?> request = HttpRequest.DELETE(RESTAPIMapping.QUESTION + "/1");
        HttpResponse<QuestionDTO> response = client.toBlocking().exchange(request, QuestionDTO.class);

        assertEquals(200, response.getStatus().getCode());
        assertEquals(1L, response.body().getId());
    }
}
