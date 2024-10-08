package mz.org.fgh.mentoring.controller.question;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.question.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Controller(RESTAPIMapping.QUESTION)
@Secured(SecurityRule.IS_AUTHENTICATED)
public class QuestionController {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @Operation(summary = "Return a list of all Questions")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Get("/getAll")
    public HttpResponse<?> getAllQuestions() {
        try {
            List<QuestionDTO> questions = questionService.getAllQuestions();
            return HttpResponse.ok(questions);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error(e.getLocalizedMessage())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Search Questions by parameters")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Get("/search")
    public HttpResponse<?> search(
            @Nullable @QueryValue("code") String code,
            @Nullable @QueryValue("description") String description,
            @Nullable @QueryValue("programId") Long programId,
            Pageable pageable
    ) {
        try {
            Page<QuestionDTO> questions = questionService.search(code, description, programId, pageable);
            return HttpResponse.ok(questions);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error(e.getLocalizedMessage())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Create a new Question")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Post("/save")
    public HttpResponse<?> create(@Body QuestionDTO questionDTO, Authentication authentication) {
        try {
            Question question = new Question(questionDTO);
            question = questionService.create(question, (Long) authentication.getAttributes().get("userInfo"));
            LOG.info("Created question {}", question);
            return HttpResponse.created(new QuestionDTO(question));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error(e.getLocalizedMessage())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Get a Question by ID")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Get("/{id}")
    public HttpResponse<?> findQuestionById(@PathVariable("id") Long id) {
        try {
            Optional<Question> questionOpt = questionService.findById(id);
            return questionOpt.map(question -> HttpResponse.ok(new QuestionDTO(question)))
                    .orElse(HttpResponse.notFound());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error(e.getLocalizedMessage())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Update an existing Question")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Patch("/update")
    public HttpResponse<?> update(@Body QuestionDTO questionDTO, Authentication authentication) {
        try {
            Optional<Question> questionOpt = questionService.findById(questionDTO.getId());
            if (questionOpt.isPresent()) {
                Question question = questionOpt.get();
                question.setCode(questionDTO.getCode());
                question.setQuestion(questionDTO.getQuestion());
                question.setProgram(new Program(questionDTO.getProgramDTO()));
                question = questionService.update(question, (Long) authentication.getAttributes().get("userInfo"));
                LOG.info("Updated question {}", question);
                return HttpResponse.ok(new QuestionDTO(question));
            } else {
                return HttpResponse.notFound();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error(e.getLocalizedMessage())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Delete a Question")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Delete("/{id}")
    public HttpResponse<?> destroyQuestion(@PathVariable("id") Long id) {
        try {
            Optional<Question> questionOpt = questionService.findById(id);
            if (questionOpt.isPresent()) {
                questionService.destroy(questionOpt.get());
                LOG.info("Deleted question with ID {}", id);
                return HttpResponse.noContent();
            } else {
                return HttpResponse.notFound();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error(e.getLocalizedMessage())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @Operation(summary = "Get paginated Questions")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Get("/getByPageAndSize")
    public HttpResponse<?> getByPageAndSize(Pageable pageable) {
        try {
            Page<QuestionDTO> questions = questionService.getByPageAndSize(pageable);
            return HttpResponse.ok(questions);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(
                    MentoringAPIError.builder()
                            .status(HttpStatus.BAD_REQUEST.getCode())
                            .error(e.getLocalizedMessage())
                            .message(e.getMessage())
                            .build()
            );
        }
    }
}
