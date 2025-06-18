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
import mz.org.fgh.mentoring.api.PaginatedResponse;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.SuccessResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.LifeCycleStatusDTO;
import mz.org.fgh.mentoring.dto.question.QuestionDTO;
import mz.org.fgh.mentoring.entity.question.Question;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.form.FormService;
import mz.org.fgh.mentoring.service.question.QuestionService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller(RESTAPIMapping.QUESTION)
@Secured(SecurityRule.IS_AUTHENTICATED)
public class QuestionController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(QuestionController.class);
    private final QuestionService questionService;

    public QuestionController(QuestionService questionService, FormService formService) {
        this.questionService = questionService;
    }

    @Operation(summary = "Return a list of all Questions")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Get("/getAll")
    public HttpResponse<?> getAllQuestions() {
        try {
            List<mz.org.fgh.mentoring.dto.question.QuestionDTO> questions = questionService.getAllQuestions();
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
            Page<mz.org.fgh.mentoring.dto.question.QuestionDTO> questions = questionService.search(code, description, programId, pageable);
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

    @Operation(summary = "Get a Question by ID")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Get("/{id}")
    public HttpResponse<?> findQuestionById(@PathVariable("id") Long id) {
        try {
            Optional<Question> questionOpt = questionService.findById(id);
            return questionOpt.map(question -> HttpResponse.ok(new mz.org.fgh.mentoring.dto.question.QuestionDTO(question)))
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

    @Operation(summary = "Get paginated Questions")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Question")
    @Get("/getByPageAndSize")
    public HttpResponse<?> getByPageAndSize(Pageable pageable) {
        try {
            Page<mz.org.fgh.mentoring.dto.question.QuestionDTO> questions = questionService.getByPageAndSize(pageable);
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

    @Operation(summary = "List or search questions areas (paginated)")
    @Get
    public HttpResponse<?> listOrSearch(@Nullable @QueryValue("name") String name,
                                        @Nullable Pageable pageable) {

        Page<Question> data = !Utilities.stringHasValue(name)
                ? questionService.findAll(resolvePageable(pageable))
                : questionService.searchByName(name, resolvePageable(pageable));

        List<QuestionDTO> dataDTOs = data.getContent().stream()
                .map(QuestionDTO::new)
                .collect(Collectors.toList());

        String message = data.getTotalSize() == 0
                ? "Sem Dados para esta pesquisa"
                : "Dados encontrados";

        return HttpResponse.ok(
                PaginatedResponse.of(
                        dataDTOs,
                        data.getTotalSize(),
                        data.getPageable(),
                        message
                )
        );
    }

    @Operation(summary = "Create a new Question")
    @Post
    public HttpResponse<?> create(@Body QuestionDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Question question = new Question(dto);
        question.setCreatedBy(userUuid);
        Question created = questionService.create(question);
        return HttpResponse.created(SuccessResponse.of("Pergunta criada com sucesso", new QuestionDTO(created)));
    }

    @Operation(summary = "Update an existing Question")
    @Put
    public HttpResponse<?> update(@Body QuestionDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Question question = new Question(dto);
        question.setUpdatedBy(userUuid);
        Question updated = questionService.update(question);
        return HttpResponse.ok(SuccessResponse.of("Pergunta atualizada com sucesso", new QuestionDTO(updated)));
    }

    @Operation(summary = "Activate or deactivate a Question by changing its LifeCycleStatus")
    @Put("/{uuid}/status")
    public HttpResponse<?> updateLifeCycleStatus(@PathVariable String uuid, @Body LifeCycleStatusDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Question updated = questionService.updateLifeCycleStatus(uuid, dto.getLifeCycleStatus(), userUuid);
        return HttpResponse.ok(SuccessResponse.of("Estado da pergunta atualizado com sucesso", new QuestionDTO(updated)));
    }

    @Operation(summary = "Delete a Question by UUID")
    @Delete("/{uuid}")
    public HttpResponse<?> delete(@PathVariable String uuid) {
        questionService.delete(uuid);
        return HttpResponse.ok(SuccessResponse.messageOnly("Pergunta eliminada com sucesso"));
    }


}
