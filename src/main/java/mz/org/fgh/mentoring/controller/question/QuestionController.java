package mz.org.fgh.mentoring.controller.question;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.service.question.QuestionService;

import java.util.ArrayList;
import java.util.List;
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.QUESTION)
public class QuestionController extends BaseController {

    @Inject
    private QuestionService questionService;

    public QuestionController() {
    }

    @Get("/{formCode}")
    public List<Question> getByFormCode(@PathVariable String formCode) {
        return questionService.getQuestionsByFormCode(formCode);
    }

    @Operation(summary = "Return a list off all Questions")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Get("/getAll")
    public List<QuestionDTO> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @Operation(summary = "Return a list of Questions given the parameters code, description and category code")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Get("/search")
    public List<QuestionDTO> search(
            @Nullable @QueryValue("code") String code,
            @Nullable @QueryValue("description") String description,
            @Nullable @QueryValue("categoryId") Long categoryId
    ) {
        List<QuestionDTO> questions = questionService.search(code, description, categoryId);
        return questions;
    }
}
