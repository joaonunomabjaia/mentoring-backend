package mz.org.fgh.mentoring.controller.question;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.question.SectionDTO;
import mz.org.fgh.mentoring.entity.question.Section;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.question.SectionService;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller(RESTAPIMapping.SECTION)
public class SectionController extends BaseController {

    private static final Logger LOG = LoggerFactory.getLogger(SectionController.class);
    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    // Get all sections
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Return a list of all Sections used in the forms")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved sections", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @Tag(name = "Sections")
    @Get(produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getAllSections() {
        try {
            List<SectionDTO> sections = listAsDtos(sectionService.getAllSections(), SectionDTO.class);
            return HttpResponse.ok(sections);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(mz.org.fgh.mentoring.error.MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage())
                    .build());
        }
    }

    // Get a section by ID
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Get a Section by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved section", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @ApiResponse(responseCode = "404", description = "Section not found")
    })
    @Tag(name = "Sections")
    @Get(uri = "/{id}", produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> getSectionById(@PathVariable Long id) {
        try {
            Optional<Section> sectionOpt = sectionService.getSection(id);
            return sectionOpt.map(section -> HttpResponse.ok(toDto(section)))
                    .orElse(HttpResponse.notFound());
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage())
                    .build());
        }
    }

    // Create a new section
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Create a new Section")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Section successfully created", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    @Tag(name = "Sections")
    @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<RestAPIResponse> createSection(@Valid @Body SectionDTO sectionDTO) {
        try {
            Section section = toEntity(sectionDTO);
            Section savedSection = sectionService.createSection(section);
            return HttpResponse.created(toDto(savedSection));
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage())
                    .build());
        }
    }

    // Update a section by ID
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Update an existing Section")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Section successfully updated", content = @Content(mediaType = MediaType.APPLICATION_JSON)),
            @ApiResponse(responseCode = "404", description = "Section not found")
    })
    @Tag(name = "Sections")
    @Put(uri = "/{id}", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
    public HttpResponse<?> updateSection(@PathVariable Long id, @Valid @Body SectionDTO sectionDTO) {
        try {
            Optional<Section> sectionOpt = sectionService.getSection(id);
            if (sectionOpt.isPresent()) {
                Section section = sectionOpt.get();
                section.setDescription(sectionDTO.getDescription()); // Example: update description
                Section updatedSection = sectionService.updateSection(section);
                return HttpResponse.ok(toDto(updatedSection));
            } else {
                return HttpResponse.notFound();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage())
                    .build());
        }
    }

    // Delete a section by ID
    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Delete a Section by its ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Section successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Section not found")
    })
    @Tag(name = "Sections")
    @Delete(uri = "/{id}")
    public HttpResponse<?> deleteSection(@PathVariable Long id) {
        try {
            Optional<Section> sectionOpt = sectionService.getSection(id);
            if (sectionOpt.isPresent()) {
                sectionService.deleteSection(id);
                return HttpResponse.noContent();
            } else {
                return HttpResponse.notFound();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage())
                    .build());
        }
    }

    private SectionDTO toDto(Section section) {
        if (section == null) {
            return null;
        }
        SectionDTO sectionDTO = new SectionDTO();
        sectionDTO.setId(section.getId());
        sectionDTO.setUuid(section.getUuid());
        sectionDTO.setCreatedBy(section.getCreatedBy());
        sectionDTO.setCreatedAt(section.getCreatedAt());
        sectionDTO.setUpdatedBy(section.getUpdatedBy());
        sectionDTO.setUpdatedAt(section.getUpdatedAt());
        sectionDTO.setLifeCycleStatus(section.getLifeCycleStatus().name());
        sectionDTO.setDescription(section.getDescription());
        return sectionDTO;
    }

    private Section toEntity(SectionDTO sectionDTO) {
        if (sectionDTO == null) {
            return null;
        }
        Section section = new Section();
        section.setId(sectionDTO.getId());
        section.setUuid(sectionDTO.getUuid());
        section.setCreatedBy(sectionDTO.getCreatedBy());
        section.setCreatedAt(sectionDTO.getCreatedAt());
        section.setUpdatedBy(sectionDTO.getUpdatedBy());
        section.setUpdatedAt(sectionDTO.getUpdatedAt());
        section.setLifeCycleStatus(sectionDTO.getLifeCycleStatus() != null
                ? LifeCycleStatus.valueOf(sectionDTO.getLifeCycleStatus())
                : null);
        section.setDescription(sectionDTO.getDescription());
        return section;
    }
}
