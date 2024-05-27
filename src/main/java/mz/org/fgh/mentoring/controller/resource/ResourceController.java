package mz.org.fgh.mentoring.controller.resource;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
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
import mz.org.fgh.mentoring.dto.resource.ResourceDTO;
import mz.org.fgh.mentoring.entity.earesource.Resource;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.repository.resource.ResourceRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Joao Nuno Mabjaia
 */
@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.RESOURCE)
public class ResourceController extends BaseController {

    private ResourceRepository resourceRepository;
    private SettingsRepository settingsRepository;
    private final UserRepository userRepository;
    public static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);

    public ResourceController(ResourceRepository resourceRepository, UserRepository userRepository, SettingsRepository settingsRepository) {
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
        this.settingsRepository = settingsRepository;
    }

    @Operation(summary = "Return a list off all Resources")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Resource")
    @Get("/getAll")
    public List<ResourceDTO> getAll() {

        List<Resource> resources = resourceRepository.findAll();
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        for (Resource resource: resources) {
            ResourceDTO resourceDTO = new ResourceDTO(resource, null);
            resourceDTOS.add(resourceDTO);
        }
        return resourceDTOS;
    }

    @Operation(summary = "Save Resource to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Resource")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body ResourceDTO resourceDTO, Authentication authentication) {
        try {
            Resource resource = new Resource(resourceDTO);
            User user = userRepository.findById((Long) authentication.getAttributes().get("userInfo")).get();
            resource.setCreatedBy(user.getUuid());
            resource.setUuid(UUID.randomUUID().toString());
            resource.setCreatedAt(DateUtils.getCurrentDate());
            resource.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
            Resource resourceResp = this.resourceRepository.save(resource);

            LOG.info("Created resource {}", resourceResp);
            return HttpResponse.ok().body(new ResourceDTO(resourceResp, null));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Update the Resources JSON")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
//    @Patch("/updateresourcetree")
    @Patch(value = "/updateresourcetree", consumes = MediaType.MULTIPART_FORM_DATA)
    @Tag(name = "Resource")
    public HttpResponse<RestAPIResponse> updateResourceTree(@Part("id") Long id,
                                                            @Part("uuid") String uuid,
                                                            @Part("resource") String resource,
                                                            @Part("file") CompletedFileUpload file,
                                                            Authentication authentication) {
        try {
            ResourceDTO resourceDTO = new ResourceDTO();
            resourceDTO.setId(id);
            resourceDTO.setUuid(uuid);
            resourceDTO.setResource(resource);
            resourceDTO.setFile(file);

            LOG.info("resourceDTO.getFile() {}", resourceDTO.getFile());
            Resource resourceEntity = new Resource(resourceDTO);
            User user = this.userRepository.fetchByUserId((Long) authentication.getAttributes().get("userInfo"));
            Optional<Resource> resourceRepositoryByUuid = this.resourceRepository.findByUuid(resourceEntity.getUuid());

            if (resourceRepositoryByUuid.isPresent()) {
                Resource existingResource = resourceRepositoryByUuid.get();
                existingResource.setResource(resourceEntity.getResource());
                existingResource.setUpdatedBy(user.getUuid());
                existingResource.setUpdatedAt(DateUtils.getCurrentDate());

                try {
                    if (file != null && file.getFilename() != null) {
                        Optional<Setting> pathFromSettings = settingsRepository.findByDesignation("ResourcesDirectory");
                        Path path = Paths.get(pathFromSettings.get().getValue(), file.getFilename());
                        Files.createDirectories(path.getParent());
                        Files.write(path, file.getBytes());
                        LOG.info("Recurso gravado.");
                    }
                } catch (IOException e) {
                    LOG.error("Falha ao gravar recurso: {}", e.getMessage());
                    return HttpResponse.serverError().body(MentoringAPIError.builder()
                            .status(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
                            .error(e.getLocalizedMessage())
                            .message("Falha ao gravar recurso no servidor").build());
                }

                Resource resourceResp = this.resourceRepository.update(existingResource);

                LOG.info("Actualizado {}", resourceResp);
                return HttpResponse.ok().body(new ResourceDTO(existingResource, null));
            } else {
                return HttpResponse.notFound().body(MentoringAPIError.builder()
                        .status(HttpStatus.NOT_FOUND.getCode())
                        .error("Recurso nao encontado")
                        .message("Recurso com uuid especificado nao encontrado").build());
            }
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }
}
