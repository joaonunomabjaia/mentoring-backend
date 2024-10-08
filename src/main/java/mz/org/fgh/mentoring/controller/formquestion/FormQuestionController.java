package mz.org.fgh.mentoring.controller.formquestion;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.form.FormQuestionDTO;
import mz.org.fgh.mentoring.entity.formQuestion.FormSectionQuestion;
import mz.org.fgh.mentoring.service.form.FormQuestionService;
import mz.org.fgh.mentoring.service.tutor.TutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.FORM_QUESTION_CONTROLLER)
@Tag(name = "FormQuestion", description = "Operations related to FormQuestions")
public class FormQuestionController extends BaseController {

    @Inject
    FormQuestionService formQuestionService;
    @Inject
    TutorService tutorService;

    public FormQuestionController() {}

    public static final Logger LOG = LoggerFactory.getLogger(FormQuestionController.class);

    @Operation(summary = "Retrieve paginated FormQuestions", description = "Fetches paginated FormQuestions using the Pageable object.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved FormQuestions")
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get
    public HttpResponse<Page<FormQuestionDTO>> getAll(@Nullable Pageable pageable) {
        LOG.debug("Searching paginated FormQuestions");
        try {
            Page<FormSectionQuestion> formQuestions = formQuestionService.findAll(pageable);
            Page<FormQuestionDTO> formQuestionDTOs = formQuestions.map(FormQuestionDTO::new); // Convert to DTO
            return HttpResponse.ok(formQuestionDTOs);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest();
        }
    }


    @Operation(summary = "Find FormQuestion by ID", description = "Fetch a FormQuestion by its ID.")
    @ApiResponse(responseCode = "200", description = "FormQuestion found")
    @ApiResponse(responseCode = "404", description = "FormQuestion not found")
    @Get("/{id}")
    public Optional<FormSectionQuestion> findById(@PathVariable Long id) {
        return formQuestionService.findById(id);
    }

    @Operation(summary = "Get paginated FormQuestions by Form ID", description = "Fetches paginated FormQuestions associated with a specific Form ID.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved FormQuestions")
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/getByFormId")
    public HttpResponse<Page<FormQuestionDTO>> findFormQuestionByFormId(@NonNull @QueryValue("formId") Long formId, @Nullable Pageable pageable) {
        LOG.debug("Searching paginated FormQuestions for formId: {}", formId);
        try {
            Page<FormQuestionDTO> formQuestionDTOs = formQuestionService.fetchByForm(formId, pageable); // Convert entities to DTOs
            return HttpResponse.ok(formQuestionDTOs);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest();
        }
    }


    @Operation(summary = "Create a new FormQuestion", description = "Create a new FormQuestion in the system.")
    @ApiResponse(responseCode = "201", description = "FormQuestion created successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public FormSectionQuestion create(@Body FormSectionQuestion formSectionQuestion) {
        LOG.debug("Created FormQuestion {}", formSectionQuestion);
        return formQuestionService.create(formSectionQuestion);
    }

    @Operation(summary = "Remove a FormQuestion", description = "Inactivate or remove a specific FormQuestion.")
    @ApiResponse(responseCode = "204", description = "FormQuestion removed successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Patch("/remove")
    public void remove(@NonNull @QueryValue("formId") Long formId, @Body FormQuestionDTO formQuestionDTO, Authentication authentication) {
        LOG.debug("Removing FormQuestion with formId: {}", formId);
        formQuestionService.inactivate((Long) authentication.getAttributes().get("userInfo"), formId, formQuestionDTO);
    }

    @Operation(summary = "Get FormQuestions by Forms UUIDs", description = "Retrieve all FormQuestions associated with specific Form UUIDs.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved FormQuestions")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Get("/getByFormsUuids")
    public List<FormQuestionDTO> findFormQuestionByFormsUuids(@NonNull @QueryValue("formsUuids") List<String> formsUuids, @QueryValue("offset") Long offset, @QueryValue("limit") Long limit) {
        return listAsDtos(formQuestionService.fetchByFormsUuids(formsUuids, offset, limit), FormQuestionDTO.class);
    }

    @Operation(summary = "Get FormQuestions by Forms UUIDs with pagination", description = "Retrieve paginated FormQuestions associated with specific Forms UUIDs.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved FormQuestions")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Get("/getByFormsUuidsAndPageAndSize")
    public List<FormQuestionDTO> findFormQuestionByFormsUuidsAndPageAndSize(@NonNull @QueryValue("formsUuids") List<String> formsUuids, @QueryValue("page") Long page, @QueryValue("size") Long size) {
        return listAsDtos(formQuestionService.fetchByFormsUuidsAndPageAndSize(formsUuids, page, size), FormQuestionDTO.class);
    }

}
