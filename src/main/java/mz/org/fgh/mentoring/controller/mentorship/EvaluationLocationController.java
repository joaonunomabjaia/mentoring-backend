package mz.org.fgh.mentoring.controller.mentorship;

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
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.mentorship.EvaluationLocationDTO;
import mz.org.fgh.mentoring.entity.mentorship.EvaluationLocation;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.mentorship.EvaluationLocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller for managing Evaluation Locations.
 */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.EVALUATION_LOCATION)
@Tag(name = "Mentorship")
public class EvaluationLocationController extends BaseController {

    private final EvaluationLocationService service;
    private static final Logger LOG = LoggerFactory.getLogger(EvaluationLocationController.class);

    public EvaluationLocationController(EvaluationLocationService service) {
        this.service = service;
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Return a list of all Evaluation Locations")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/getAll")
    public HttpResponse<?> getAll(Pageable pageable) {
        try {
            return HttpResponse.ok(service.findAll(pageable));
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

    @Operation(summary = "Save Evaluation Location to the database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Post("/save")
    public HttpResponse<RestAPIResponse> create(@Body EvaluationLocationDTO dto, Authentication authentication) {
        EvaluationLocation location = new EvaluationLocation(dto);
        location = service.create(location, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created evaluation location {}", location);

        return HttpResponse.ok().body(new EvaluationLocationDTO(location));
    }

    @Operation(summary = "Get Evaluation Location from the database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/{id}")
    public EvaluationLocationDTO findById(@PathVariable Long id) {
        EvaluationLocation location = service.findById(id).orElse(null);
        return new EvaluationLocationDTO(location);
    }

    @Operation(summary = "Update Evaluation Location in the database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Patch("/update")
    public HttpResponse<RestAPIResponse> update(@Body EvaluationLocationDTO dto, Authentication authentication) {
        EvaluationLocation location = service.findById(dto.getId()).orElse(null);
        if (location == null) {
            return HttpResponse.notFound();
        }

        location.setCode(dto.getCode());
        location.setDescription(dto.getDescription());
        location = service.update(location, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Updated evaluation location {}", location);

        return HttpResponse.ok().body(new EvaluationLocationDTO(location));
    }

    @Operation(summary = "Delete Evaluation Location from the database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Patch("/{id}")
    public EvaluationLocationDTO delete(@PathVariable Long id, Authentication authentication) {
        EvaluationLocation location = service.findById(id).orElse(null);
        if (location == null) {
            return null;
        }

        location = service.delete(location, (Long) authentication.getAttributes().get("userInfo"));
        return new EvaluationLocationDTO(location);
    }

    @Operation(summary = "Destroy Evaluation Location from the database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Delete("/{id}")
    public EvaluationLocationDTO destroy(@PathVariable Long id, Authentication authentication) {
        EvaluationLocation location = service.findById(id).orElse(null);
        if (location == null) {
            return null;
        }

        service.destroy(location);
        return new EvaluationLocationDTO(location);
    }
}
