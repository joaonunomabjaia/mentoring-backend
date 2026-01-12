package mz.org.fgh.mentoring.controller.tutored;

import io.micronaut.core.annotation.NonNull;
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
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.*;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.program.ProgramDTO;
import mz.org.fgh.mentoring.dto.tutored.TutoredDTO;
import mz.org.fgh.mentoring.entity.tutored.FlowHistory;
import mz.org.fgh.mentoring.entity.tutored.FlowHistoryProgressStatus;
import mz.org.fgh.mentoring.entity.program.Program;
import mz.org.fgh.mentoring.entity.tutored.MenteeFlowHistory;
import mz.org.fgh.mentoring.entity.tutored.Tutored;
import mz.org.fgh.mentoring.enums.EnumFlowHistory;
import mz.org.fgh.mentoring.enums.EnumFlowHistoryProgressStatus;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.service.tutored.FlowHistoryProgressStatusService;
import mz.org.fgh.mentoring.service.tutored.FlowHistoryService;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.TUTORED_CONTROLLER)
public class TutoredController extends BaseController {

    @Inject
    TutoredService tutoredService;

    @Inject
    FlowHistoryService  flowHistoryService;

    @Inject
    FlowHistoryProgressStatusService flowHistoryProgressStatusService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TutoredController.class);

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/{offset}/{limit}")
    public List<TutoredDTO> getAll(@PathVariable("offset") long offset, @PathVariable("limit") long limit) {
        List<TutoredDTO> tutoredDTOS = tutoredService.findAll(offset, limit);
        return tutoredDTOS;
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/tutoreds")
    public List<TutoredDTO> getTutoredAll() {
        List<TutoredDTO> tutoredDTOs = new ArrayList<>();

        tutoredDTOs =  tutoredService.searchTutored(4l, null, null, null);
        return tutoredDTOs;
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutored")
    @Get("/getTutoreds")
    public List<TutoredDTO> getTutoreds(@QueryValue("uuids") List<String> uuids, @QueryValue("offset") Long offset, @QueryValue("limit") Long limit) {
        List<Tutored> tutoreds = tutoredService.getTutoredsByHealthFacilityUuids(uuids, offset, limit);
        try {
            return Utilities.parseList(tutoreds, TutoredDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    @Get("/tutor/{tutorUuid}")
    public List<TutoredDTO> getTutoredByTutorUuid(@PathVariable("tutorUuid") String tutorUuid){

        LOGGER.debug(" get Tutored By Tutor Uuid {}", tutorUuid);
        List<TutoredDTO> tutoredDTOS = this.tutoredService.findTutorByUserUuid(tutorUuid);
        return tutoredDTOS;
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutored")
    @Get("/tutored/{uuid}")
    public List<TutoredDTO> getTutoredByUuid(@PathVariable("uuid") String tutorUuid){

        List<TutoredDTO> tutoredDTOS = this.tutoredService.findTutoredByUuid(tutorUuid);
        return tutoredDTOS;
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "mentorados")
    @Get("/search")
    public List<TutoredDTO> searchTutored(@NonNull @QueryValue("userId") Long userId,
                                          @Nullable @QueryValue("nuit") Long nuit,
                                          @Nullable @QueryValue("name") String name,
                                          @Nullable @QueryValue("phoneNumber") String phoneNumber) {


        List<TutoredDTO> tutoredDTOs = new ArrayList<>();

        tutoredDTOs =  tutoredService.searchTutored(userId, nuit, name, phoneNumber);

        return tutoredDTOs;
    }

    @Deprecated
    @Operation(summary = "Save Mentorados to database")
    @Tag(name = "mentorados")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Patch("/update")
    public HttpResponse<RestAPIResponse> updateTutored(@Body TutoredDTO tutoredDTO , Authentication authentication){
        try {
            TutoredDTO respo = this.tutoredService.updateTutored(tutoredDTO, (Long) authentication.getAttributes().get("userInfo"));
            return HttpResponse.ok().body(respo);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Update an existing mentee")
    @Put
    public HttpResponse<?> update(@Body TutoredDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        Tutored tutored = new Tutored(dto);
        tutored.addFlowHistory(new MenteeFlowHistory(dto.getFlowHistoryMenteeAuxDTO().get(0)));

        tutored.setUpdatedBy(userUuid);
        Tutored res = this.tutoredService.update(tutored, userUuid);

        TutoredDTO updatedTutoredDTO = new TutoredDTO(res);

        return HttpResponse.ok(SuccessResponse.of("Mentorado atualizado com sucesso", updatedTutoredDTO));
    }

    @Operation(summary = "Save Mentee to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentee")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body TutoredDTO dto, Authentication auth) {
        try {
            Tutored tutored = new Tutored(dto);

            // DTO brings codes now
            String estagioCode = dto.getFlowHistoryMenteeAuxDTO().get(0).estagio();
            String estadoCode  = dto.getFlowHistoryMenteeAuxDTO().get(0).estado();

            FlowHistory flowHistory = flowHistoryService.findByCode(estagioCode)
                    .orElseThrow(() -> new RuntimeException("FlowHistory não encontrado (code): " + estagioCode));

            FlowHistoryProgressStatus fhps = flowHistoryProgressStatusService.findByCode(estadoCode)
                    .orElseThrow(() -> new RuntimeException("FlowHistoryProgressStatus não encontrado (code): " + estadoCode));

            Tutored createdTutored =  this.tutoredService.create(
                    tutored, flowHistory, fhps, (Long) auth.getAttributes().get("userInfo")
            );
            TutoredDTO tutoredDTO = new TutoredDTO(createdTutored);
            return HttpResponse.created(tutoredDTO);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }


    @Operation(summary = "Batch update mentees to database")
    @ApiResponse(responseCode = "200", description = "Mentee updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid data provided")
    @Patch("/batch-update")
    public HttpResponse<RestAPIResponse> updateTutoredBatch(
            @Body List<TutoredDTO> tutoredDTOs, 
            Authentication authentication) {
        try {
            String userUuid = (String) authentication.getAttributes().get("useruuid");
    
            // Loop through each TutoredDTO and update it
            for (TutoredDTO dto : tutoredDTOs) {
                tutoredService.update(new Tutored(dto), userUuid);
            }
    
            return HttpResponse.ok(new RestAPIResponseImpl(
                HttpStatus.OK.getCode(), 
                "All tutoreds updated successfully"));
        } catch (Exception e) {
            LOGGER.error("Error during batch update of tutoreds: ", e);
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Get mentee by UUID")
    @Get("/{uuid}")
    public HttpResponse<?> findById(@PathVariable String uuid) {
        Optional<Tutored> optional = tutoredService.findOptionalByUuid(uuid);
        return optional.map(tutored ->
                HttpResponse.ok(SuccessResponse.of("Mentorando encontrado com sucesso", new TutoredDTO(tutored)))
        ).orElse(HttpResponse.notFound());
    }

    @Operation(summary = "List or search Mentees (paginated)")
    @Get
    public HttpResponse<?> listOrSearch(@Nullable @QueryValue("uuids") List<String> uuids,
                                        @Nullable Pageable pageable) {

        Page<Tutored> groups = !Utilities.listHasElements(uuids)
                ? tutoredService.findAll(resolvePageable(pageable))
                : tutoredService.getTutoredsByHealthFacilityUuids(uuids, resolvePageable(pageable));

        List<TutoredDTO> groupDTOs = groups.getContent().stream()
                .map(TutoredDTO::new)
                .collect(Collectors.toList());

        String message = groups.getTotalSize() == 0
                ? "Sem Dados para esta pesquisa"
                : "Dados encontrados";

        return HttpResponse.ok(
                PaginatedResponse.of(
                        groupDTOs,
                        groups.getTotalSize(),
                        groups.getPageable(),
                        message
                )
        );
    }
}
