package mz.org.fgh.mentoring.controller.session;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Consumes;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.SecurityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.session.SessionRecommendedResourceDTO;
import mz.org.fgh.mentoring.entity.session.SessionRecommendedResource;
import mz.org.fgh.mentoring.service.session.SessionRecommendedResourceService;

import java.util.List;
import java.util.stream.Collectors;

@Controller(RESTAPIMapping.SESSIONS_REOMMENDED_RESOURCES)
@Secured(SecurityRule.IS_AUTHENTICATED)
public class SessionRecommendedResourceController extends BaseController {

    @Inject
    private SecurityService securityService;

    @Inject
    private final SessionRecommendedResourceService sessionRecommendedResourceService;

    public SessionRecommendedResourceController(SessionRecommendedResourceService sessionRecommendedResourceService) {
        this.sessionRecommendedResourceService = sessionRecommendedResourceService;
    }

    @Operation(summary = "Return a list of all Session Recommended Resources")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "SessionRecommendedResource")
    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<List<SessionRecommendedResourceDTO>> getAll() {
        List<SessionRecommendedResource> resources = sessionRecommendedResourceService.findAll();
        return HttpResponse.ok(resources.stream().map(SessionRecommendedResourceDTO::new).collect(Collectors.toList()));
    }

    @Operation(summary = "Save a new Session Recommended Resource")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "SessionRecommendedResource")
    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<RestAPIResponse> save(@Body SessionRecommendedResourceDTO sessionRecommendedResourceDTO) {
        Long userId = (Long) securityService.getAuthentication().get().getAttributes().get("userInfo");
        SessionRecommendedResource resource = convertToEntity(sessionRecommendedResourceDTO);
        SessionRecommendedResource savedResource = sessionRecommendedResourceService.save(resource, userId);
        return HttpResponse.created(new SessionRecommendedResourceDTO(savedResource));
    }

    @Operation(summary = "Update an existing Session Recommended Resource")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "SessionRecommendedResource")
    @Put("/{uuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<RestAPIResponse> update(@PathVariable String uuid, @Body SessionRecommendedResourceDTO sessionRecommendedResourceDTO) {
        SessionRecommendedResource resource = convertToEntity(sessionRecommendedResourceDTO);
        Long userId = (Long) securityService.getAuthentication().get().getAttributes().get("userInfo");

        SessionRecommendedResource updatedResource = sessionRecommendedResourceService.updateByUuid(uuid, resource, userId);
        return HttpResponse.ok(new SessionRecommendedResourceDTO(updatedResource));
    }

    @Operation(summary = "Get Session Recommended Resource by Tutored UUID and Resource Link")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "SessionRecommendedResource")
    @Get("/tutored/{tutoredUuid}/resourceLink/{resourceLink}")
    @Produces(MediaType.APPLICATION_JSON)
    public HttpResponse<RestAPIResponse> getByTutoredAndResourceLink(@PathVariable String tutoredUuid, @PathVariable String resourceLink) {
        SessionRecommendedResource resource = sessionRecommendedResourceService.findByTutoredUuidAndResourceLink(tutoredUuid, resourceLink);
        return HttpResponse.ok(new SessionRecommendedResourceDTO(resource));
    }

    private SessionRecommendedResource convertToEntity(SessionRecommendedResourceDTO dto) {
        return sessionRecommendedResourceService.createEntityFromDto(dto);
    }
}
