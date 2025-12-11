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
import lombok.NonNull;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.resource.ResourceDTO;
import mz.org.fgh.mentoring.entity.earesource.Resource;
import mz.org.fgh.mentoring.entity.session.SessionRecommendedResource;
import mz.org.fgh.mentoring.entity.setting.Setting;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.repository.resource.ResourceRepository;
import mz.org.fgh.mentoring.repository.session.SessionRecommendedResourceRepository;
import mz.org.fgh.mentoring.repository.settings.SettingsRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.resource.ResourceService;
import mz.org.fgh.mentoring.service.setting.SettingService;
import mz.org.fgh.mentoring.util.DateUtils;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

import static mz.org.fgh.mentoring.config.SettingKeys.RESOURCES_DIRECTORY;

/**
 * @author Joao Nuno Mabjaia
 */

@Controller(RESTAPIMapping.RESOURCE)
public class ResourceController extends BaseController {

    private ResourceRepository resourceRepository;
    private final UserRepository userRepository;
    private final ResourceService resourceService;
    private final SettingService settings;

    private SessionRecommendedResourceRepository sessionRecommendedResourceRepository;
    public static final Logger LOG = LoggerFactory.getLogger(ResourceController.class);

    public ResourceController(ResourceRepository resourceRepository, UserRepository userRepository, ResourceService resourceService, SettingService settings, SessionRecommendedResourceRepository sessionRecommendedResourceRepository) {
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
        this.resourceService = resourceService;
        this.settings = settings;
        this.sessionRecommendedResourceRepository = sessionRecommendedResourceRepository;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Return a list off all Resources")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Resource")
    @Get("/getAll")
    public List<ResourceDTO> getAll() {

        List<Resource> resources = resourceRepository.findAll();
        List<ResourceDTO> resourceDTOS = new ArrayList<>();
        for (Resource resource : resources) {
            ResourceDTO resourceDTO = new ResourceDTO(resource, null);
            resourceDTOS.add(resourceDTO);
        }
        return resourceDTOS;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Save Resource to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Resource")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create(@Body ResourceDTO resourceDTO, Authentication authentication) {
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

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Update the Resources JSON")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Patch("/updateresourcetreewithoutfile")
    @Tag(name = "Resource")
    public HttpResponse<RestAPIResponse> updateResourceTreeWithoutFile(@NonNull @Body ResourceDTO resourceDTO, Authentication authentication) {
        try {
            Resource resource = new Resource(resourceDTO);
            User user = this.userRepository.fetchByUserId((Long) authentication.getAttributes().get("userInfo"));
            Optional<Resource> resourceRepositoryByUuid = this.resourceRepository.findByUuid(resource.getUuid());
            if (resourceRepositoryByUuid.isPresent()) {
                resourceRepositoryByUuid.get().setResource(resource.getResource());
                resourceRepositoryByUuid.get().setUpdatedBy(user.getUuid());
                resourceRepositoryByUuid.get().setUpdatedAt(DateUtils.getCurrentDate());
                Resource resourceResp = this.resourceRepository.update(resourceRepositoryByUuid.get());

                LOG.info("Updated resource {}", resourceResp);
                return HttpResponse.ok().body(new ResourceDTO(resourceRepositoryByUuid.get(), null));
            }
            return null;
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Update the Resources JSON")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
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
                        Path path = Paths.get(settings.get(RESOURCES_DIRECTORY, "/srv/his/mentoring/backend/ea_resources"), file.getFilename());
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

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Obter recurso do servidor")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM))
    @Get("/load")
    @Tag(name = "Resource")
    public HttpResponse<?> loadFile(@QueryValue String fileName) {
        try {
                Path filePath = Paths.get(settings.get(RESOURCES_DIRECTORY, "/srv/his/mentoring/backend/ea_resources"), fileName);
                if (Files.exists(filePath)) {
                    byte[] fileBytes = Files.readAllBytes(filePath);
                    return HttpResponse.ok()
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .contentLength(fileBytes.length)
                            .body(fileBytes);
                } else {
                    return HttpResponse.notFound().body(MentoringAPIError.builder()
                            .status(HttpStatus.NOT_FOUND.getCode())
                            .error("Arquivo não encontrado")
                            .message("Arquivo com o nome especificado não encontrado").build());
                }
        } catch (Exception e) {
            LOG.error("Erro ao buscar arquivo de recurso: {}", e.getMessage());
            return HttpResponse.serverError().body(MentoringAPIError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
                    .error(e.getLocalizedMessage())
                    .message("Erro ao buscar arquivo de recurso").build());
        }
    }


    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Obter recurso do servidor")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM))
    @Get("/load/documento/")
    @Tag(name = "Resource")
    public HttpResponse<?> loadFileByToken(@QueryValue String nuit, @QueryValue String token, @QueryValue String fileName) {

        Optional<SessionRecommendedResource> sessionRecommendedResource = this.sessionRecommendedResourceRepository.findByToken(token);

        if (!sessionRecommendedResource.isPresent()) {
            return HttpResponse.serverError().body(MentoringAPIError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
                    .error("Token não encontrada")
                    .message("Acesso recusado").build());
        }

        if (Integer.toString(sessionRecommendedResource.get().getTutored().getEmployee().getNuit()).equals(nuit)) {

            try {
                    Path filePath = Paths.get(settings.get(RESOURCES_DIRECTORY, "/srv/his/mentoring/backend/ea_resources"), fileName);
                    if (Files.exists(filePath)) {
                        byte[] fileBytes = Files.readAllBytes(filePath);

                        HttpResponse<?> results = HttpResponse.ok()
                                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                .contentLength(fileBytes.length)
                                .body(fileBytes);

                        URI link = new URI("file://"+filePath.toString());

                        Desktop.getDesktop().browse(link);

                        return results;
                    } else {
                        return HttpResponse.notFound().body(MentoringAPIError.builder()
                                .status(HttpStatus.NOT_FOUND.getCode())
                                .error("Arquivo não encontrado")
                                .message("Arquivo com o nome especificado não encontrado").build());
                    }
            } catch (Exception e) {
                LOG.error("Erro ao buscar arquivo de recurso: {}", e.getMessage());
                return HttpResponse.serverError().body(MentoringAPIError.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
                        .error(e.getLocalizedMessage())
                        .message("Erro ao buscar arquivo de recurso").build());
            }

        } else {

            return HttpResponse.serverError().body(MentoringAPIError.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.getCode())
                    .error("Token ou o NUIT errado ")
                    .message("Acesso recusado ").build());

        }
    }

    /*@Secured(SecurityRule.IS_AUTHENTICATED)
    @Operation(summary = "Extrai nome e descrição de todos os recursos do JSON")
    @Get("/summaries/from-json")
    @Tag(name = "Resource")
    public HttpResponse<List<Map<String, String>>> getJsonResourceSummaries() {
        List<Map<String, String>> summaries = resourceService.extractResourceSummariesFromJson();
        return HttpResponse.ok(summaries);
    }*/


}
