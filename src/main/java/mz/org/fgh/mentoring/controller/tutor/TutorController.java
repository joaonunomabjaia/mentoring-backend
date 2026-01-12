package mz.org.fgh.mentoring.controller.tutor;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.version.annotation.Version;
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
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.tutor.TutorDTO;
import mz.org.fgh.mentoring.dto.tutor.TutorInternalLocationDTO;
import mz.org.fgh.mentoring.entity.programaticarea.ProgrammaticArea;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.entity.tutorprogramaticarea.TutorProgrammaticArea;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.repository.programaticarea.ProgramaticAreaRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.programaticarea.ProgramaticAreaService;
import mz.org.fgh.mentoring.service.tutor.TutorInternalLocationService;
import mz.org.fgh.mentoring.service.tutor.TutorService;
import mz.org.fgh.mentoring.service.tutorprogrammaticarea.TutorProgrammaticAreaService;
import mz.org.fgh.mentoring.util.LifeCycleStatus;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("mentor")
public class TutorController extends BaseController {

    @Inject
    private TutorService tutorService;

    @Inject
    private ProgramaticAreaRepository programaticAreaRepository;

    @Inject
    private TutorProgrammaticAreaService tutorProgrammaticAreaService;

    @Inject
    private ProgramaticAreaService programmaticAreaService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TutorInternalLocationService tutorInternalLocationService;

    public TutorController() {
    }

    public static final Logger LOG = LoggerFactory.getLogger(TutorController.class);

    @Operation(summary = "Return a list off all Tutor")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Tutor")
    @Version(API_VERSION)
    @Get("/{limit}/{offset}")
    public List<TutorDTO> getAll(@PathVariable("limit") long limit , @PathVariable("offset") long offset) {
        LOG.debug("Searching tutors version 2");
        List<Tutor> tutors;
        List<TutorDTO> tutorDTOS = new ArrayList<>();

        if(limit > 0){
            tutors = this.tutorService.findTutorWithLimit(limit, offset);
        }else {
            tutors =  tutorService.findAll();
        }

        for (Tutor tutor : tutors){
            TutorDTO dto = new TutorDTO(tutor);

            tutorInternalLocationService
                    .findActiveByTutorUuid(tutor.getUuid())
                    .ifPresent(il ->
                            dto.setInternalLocationDTO(
                                    new TutorInternalLocationDTO(il)
                            )
                    );

            tutorDTOS.add(dto);
        }
        return tutorDTOS;
    }

    @Operation(summary = "Return a list off all Tutor")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentor")
    @Get("/employee/{uuid}")
    public TutorDTO getTutorByEmployeeUuid(@PathVariable("uuid") String uuid) {

        Tutor tutor = tutorService.getTutorByEmployeeUuid(uuid);
        TutorDTO dto = new TutorDTO(tutor);

        tutorInternalLocationService
                .findActiveByTutorUuid(tutor.getUuid())
                .ifPresent(il ->
                        dto.setInternalLocationDTO(
                                new TutorInternalLocationDTO(il)
                        )
                );

        return dto;
    }
    @Operation(summary = "Return a list off all Tutor")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentor")
    @Get("/search")
    public List<TutorDTO> search(@Nullable @QueryValue("name") String name,
                                 @Nullable @QueryValue("nuit") Long nuit,
                                 @NonNull @QueryValue("userId") Long userId,
                                 @Nullable @QueryValue("phoneNumber") String phoneNumber) {
        LOG.debug("Searching tutors ....");
        List<Tutor> tutors;
        List<TutorDTO> tutorDTOS = new ArrayList<>();

        tutors =  tutorService.search(name, nuit, userId, phoneNumber);


        for (Tutor tutor : tutors) {
            List<TutorProgrammaticArea> tutorProgrammaticAreas = tutorProgrammaticAreaService.fetchAllTutorProgrammaticAreas(tutor.getId());
            tutor.setTutorProgrammaticAreas(tutorProgrammaticAreas);
            for (TutorProgrammaticArea t: tutor.getTutorProgrammaticAreas()) {
                t.setProgrammaticArea(programmaticAreaService.getProgrammaticAreaById(t.getProgrammaticArea().getId()));
            }

            TutorDTO dto = new TutorDTO(tutor);

            tutorInternalLocationService
                    .findActiveByTutorUuid(tutor.getUuid())
                    .ifPresent(il ->
                            dto.setInternalLocationDTO(
                                    new TutorInternalLocationDTO(il)
                            )
                    );

            tutorDTOS.add(dto);
        }
        return tutorDTOS;
    }

    @Get
    public List<TutorDTO> getAllV1() {
        LOG.debug("Searching tutors version 1");

        List<Tutor> tutors;
        List<TutorDTO> tutorDTOS = new ArrayList<>();

        tutors = tutorService.findAll();

        if (!Utilities.listHasElements((ArrayList<?>) tutors)) return null;

        for (Tutor tutor : tutors){
            TutorDTO dto = new TutorDTO(tutor);

            tutorInternalLocationService
                    .findActiveByTutorUuid(tutor.getUuid())
                    .ifPresent(il ->
                            dto.setInternalLocationDTO(
                                    new TutorInternalLocationDTO(il)
                            )
                    );

            tutorDTOS.add(dto);
        }

        return tutorDTOS;
    }

    @Get("/{id}")
    public TutorDTO findTutorById(@PathVariable("id") Long id){

        Tutor tutor = this.tutorService.findById(id).get();
        return new TutorDTO(tutor);
    }

    @Operation(summary = "Save Mentor to database and generate correspondent user")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentor")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body TutorDTO tutorDTO, Authentication authentication) {
        TutorProgrammaticArea tutorProgrammaticArea;
        User user = userRepository.findById((Long) authentication.getAttributes().get("userInfo")).get();
        try {
            Tutor tutor = new Tutor(tutorDTO);
            tutor = this.tutorService.create(tutor, user.getId());

            if (tutorDTO.getInternalLocationDTO() != null) {
                tutorInternalLocationService.assignInternalLocation(
                        tutor.getUuid(),
                        tutorDTO.getInternalLocationDTO()
                                .getHealthFacilityDTO()
                                .getUuid(),
                        user
                );
            }


            List<ProgrammaticArea> programmaticAreas = programaticAreaRepository.findAll();

            for (ProgrammaticArea programmaticArea: programmaticAreas) {
                tutorProgrammaticArea = new TutorProgrammaticArea();
                tutorProgrammaticArea.setTutor(tutor);
                tutorProgrammaticArea.setProgrammaticArea(programmaticArea);
                tutorProgrammaticArea.setCreatedBy(user.getUuid());
                tutorProgrammaticArea.setCreatedAt(new Date());
                tutorProgrammaticArea.setLifeCycleStatus(LifeCycleStatus.ACTIVE);
                tutor.getTutorProgrammaticAreas().add(tutorProgrammaticAreaService.create(tutorProgrammaticArea, user.getId()));
            }

            LOG.info("Created mentor {}", tutor);
            return HttpResponse.created(new TutorDTO(tutor));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Update Mentor to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Mentor")
    @Patch("/update")
    public HttpResponse<RestAPIResponse> update (@Body TutorDTO tutorDTO, Authentication authentication) {
        try {
            Tutor tutor = new Tutor(tutorDTO);
            tutor = this.tutorService.update(tutor, (Long) authentication.getAttributes().get("userInfo"));
            LOG.info("Updated mentor {}", tutor);
            return HttpResponse.ok().body(new TutorDTO(tutor));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }
}