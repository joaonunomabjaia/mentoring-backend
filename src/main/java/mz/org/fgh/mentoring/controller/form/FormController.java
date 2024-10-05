package mz.org.fgh.mentoring.controller.form;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
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
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.form.FormService;
import mz.org.fgh.mentoring.util.Utilities;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.FORM_CONTROLLER)
@Tag(name = "Form", description = "Form API for managing forms")
public class FormController {

    @Inject
    FormService formService;
    public FormController() {}

    public static final Logger LOG = LoggerFactory.getLogger(FormController.class);

    @Operation(summary = "Retrieve all forms with pagination", description = "Fetches all forms with pagination using the Pageable object.")
    @ApiResponse(responseCode = "200", description = "Forms retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = MentoringAPIError.class)))
    @Get("/form")
    public HttpResponse<?> getAll(@Nullable Pageable pageable) {
        try {
            Page<FormDTO> forms = formService.findAll(pageable);
            return HttpResponse.ok(forms);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }


    @Operation(summary = "Find form by ID", description = "Retrieves a specific form by its ID.")
    @ApiResponse(responseCode = "200", description = "Form found")
    @ApiResponse(responseCode = "404", description = "Form not found")
    @Get("/getById/{id}")
    public HttpResponse<?> findById(@PathVariable("id") Long id) {
        try {
            Optional<Form> form = formService.findById(id);
            return form.map(HttpResponse::ok)
                    .orElse(HttpResponse.notFound());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Search forms with filters", description = "Searches forms based on provided filters: code, name, program, and programmatic area code.")
    @ApiResponse(responseCode = "200", description = "Forms retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Get("/search")
    public HttpResponse<?> findBySelectedFilter(@Nullable @QueryValue("code") String code,
                                                @Nullable @QueryValue("name") String name,
                                                @Nullable @QueryValue("program") String program,
                                                @Nullable @QueryValue("programmaticAreaCode") String programmaticAreaCode) {
        try {
            List<FormDTO> forms = formService.findBySelectedFilter(
                    StringUtils.defaultString(code),
                    StringUtils.defaultString(name),
                    StringUtils.defaultString(program),
                    StringUtils.defaultString(programmaticAreaCode));
            return HttpResponse.ok(forms);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Find forms by programmatic area UUID", description = "Retrieves forms associated with the given programmatic area UUID.")
    @ApiResponse(responseCode = "200", description = "Forms retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Get("/programaticarea/{progrArea}")
    public HttpResponse<?> findFormByProgrammaticAreaUuid(@PathVariable("progrArea") String progrArea) {
        try {
            List<FormDTO> forms = formService.findFormByProgrammaticAreaUuid(progrArea);
            return HttpResponse.ok(forms);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Find forms by tutor UUID", description = "Retrieves forms associated with the given tutor UUID.")
    @ApiResponse(responseCode = "200", description = "Forms retrieved successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Get("/getByTutorUuidd/{tutorUuid}")
    public HttpResponse<?> getByTutorUuidd(@PathVariable("tutorUuid") String tutorUuid) {
        try {
            List<Form> forms = formService.getByTutorUuid(tutorUuid);
            if (Utilities.listHasElements(forms)) {
                return HttpResponse.ok(Utilities.parseList(forms, FormDTO.class));
            }
            return HttpResponse.ok(new ArrayList<>());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Save or update a form", description = "Saves a new form or updates an existing one.")
    @ApiResponse(responseCode = "201", description = "Form saved or updated successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Post("/saveOrUpdate")
    public HttpResponse<?> saveOrUpdate(@NonNull @Body FormDTO formDTO, Authentication authentication) {
        try {
            formDTO.setCode(null);
            FormDTO savedForm = formService.saveOrUpdate((Long) authentication.getAttributes().get("userInfo"), formDTO);
            return HttpResponse.created(savedForm);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Update the form life cycle status", description = "Updates the life cycle status of a form.")
    @ApiResponse(responseCode = "200", description = "Form status updated successfully")
    @ApiResponse(responseCode = "400", description = "Bad request")
    @Patch("/changeLifeCicleStatus")
    public HttpResponse<?> changeLifeCicleStatus(@NonNull @Body FormDTO formDTO, Authentication authentication) {
        try {
            Form updatedForm = formService.updateLifeCycleStatus(formDTO.toForm(), (Long) authentication.getAttributes().get("userInfo"));
            return HttpResponse.ok(new FormDTO(updatedForm));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }
}
