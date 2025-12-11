package mz.org.fgh.mentoring.controller.ronda;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
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
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.ronda.RondaDTO;
import mz.org.fgh.mentoring.dto.ronda.RondaReportDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.error.MentoringAPIError;
import mz.org.fgh.mentoring.repository.ronda.RondaMenteeRepository;
import mz.org.fgh.mentoring.repository.ronda.RondaMentorRepository;
import mz.org.fgh.mentoring.repository.user.UserRepository;
import mz.org.fgh.mentoring.service.ronda.RondaService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.RONDA)
public class RondaController extends BaseController {

    @Inject
    private RondaService rondaService;

    @Inject
    private RondaMenteeRepository rondaMenteeRepository;

    @Inject
    private RondaMentorRepository rondaMentorRepository;

    @Inject
    private UserRepository userRepository;

    public static final Logger LOG = LoggerFactory.getLogger(RondaController.class.getSimpleName());
    public RondaController() {
    }

    @Operation(summary = "Return a list off all Round")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("getall")
    public List<RondaDTO> getAll() {
        LOG.debug("Searching Ronda version 2");

        List<Ronda> rondas = new ArrayList<>();
        List<RondaDTO> rondaDTOs = new ArrayList<>();

        rondas = this.rondaService.findAll();

        for(Ronda ronda : rondas){
            ronda.setRondaMentees(rondaMenteeRepository.findByRonda(ronda.getId()));
            ronda.setRondaMentors(rondaMentorRepository.findByRonda(ronda.getId()));
            rondaDTOs.add(new RondaDTO(ronda));
        }
        return rondaDTOs;
    }

    @Get
    public List<RondaDTO> getAllV1() {
        LOG.debug("Searching Rondas version 1");

        List<Ronda> rondas = new ArrayList<>();
        List<RondaDTO> rondaDTOS = new ArrayList<>();

        rondas = this.rondaService.findAll();

        for(Ronda ronda : rondas){
            rondaDTOS.add(new RondaDTO(ronda));
        }
        return rondaDTOS;
    }

    @Operation(summary = "Search Rondas with filters")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Get("/search")
    public List<RondaDTO> search(@QueryValue("province") Long provinceId,
                                 @Nullable @QueryValue("district") Long districtId,
                                 @Nullable @QueryValue("healthFacility") Long healthFacilityId,
                                 @Nullable @QueryValue("mentor") Long mentorId,
                                 @Nullable @QueryValue("startDate") String startDate,
                                 @Nullable @QueryValue("endDate") String endDate) {
        LOG.debug("Searching rondas with filters...");

        List<Ronda> rondas = rondaService.search(provinceId, districtId, healthFacilityId, mentorId, startDate, endDate);

        List<RondaDTO> rondaDTOS = new ArrayList<>();

        for (Ronda ronda : rondas) {
            ronda.setRondaMentors(rondaMentorRepository.findByRonda(ronda.getId()));
            ronda.setRondaMentees(rondaMenteeRepository.findByRonda(ronda.getId()));
            rondaDTOS.add(new RondaDTO(ronda));
        }
        return rondaDTOS;
    }

    @Operation(summary = "Change mentor of a Ronda")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Post("/changeMentor/{rondaId}/{newMentorId}")
    public HttpResponse<RestAPIResponse> changeMentor(@Body Long rondaId, Long newMentorId, Authentication authentication)
    {
        User user = userRepository.findById((Long) authentication.getAttributes().get("userInfo")).orElse(null);

        if (user == null) {
            return HttpResponse.unauthorized();
        }

        try {
            RondaDTO updatedRonda = rondaService.changeMentor(rondaId, newMentorId, user);
            return HttpResponse.ok(updatedRonda);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                .status(HttpStatus.BAD_REQUEST.getCode())
                .error(e.getLocalizedMessage())
                .message(e.getMessage()).build());
        }
    }



    @Get("/{id}")
    public RondaDTO findRondaById(@PathVariable("id") Long id){

         Ronda ronda =  this.rondaService.findById(id).get();
        return new RondaDTO(ronda);
    }

    @Get("/ronda/{uuid}")
    public RondaDTO findRondaByUuid(@PathVariable("uuid") String uuid){

        Ronda ronda = this.rondaService.findByUuid(uuid).get();
        return new RondaDTO(ronda);
    }

    @Operation(summary = "Return a list off all Rounds")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Get("/getAllRondasOfMentor")
    public List<RondaDTO> getAllRondasOfMentor(@QueryValue("mentorId") Long mentorId) {
        List<RondaDTO> rondas = this.rondaService.getAllRondasOfMentor(mentorId);
        return rondas;
    }

    @Operation(summary = "Saves a list of Rounds")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Patch(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            value = "/closeRonda"
    )
    public List<RondaDTO> closeRonda(@Body List<RondaDTO> rondaDTOS, Authentication authentication) {
        List<Ronda> rondas = Utilities.parse(rondaDTOS, Ronda.class);
        return listAsDtos(this.rondaService.updateMany(rondas, (Long) authentication.getAttributes().get("userInfo")), RondaDTO.class);
    }

    @Operation(summary = "Save Ronda")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            value = "/save"
    )
    public HttpResponse<RestAPIResponse> postRonda (@Body RondaDTO rondaDTO, Authentication authentication) {
        try {
        RondaDTO dto = this.rondaService.createRonda(rondaDTO, (Long) authentication.getAttributes().get("userInfo"));

        return HttpResponse.created(dto);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Return a list off all Rounds of a given mentor")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Get("/getAllOfMentor")
    public HttpResponse<?> getAllOfMentor(@QueryValue("mentorUuid") String mentorUuid) {
        try {
            List<Ronda> rondas = rondaService.getAllOfMentors(Collections.singletonList(mentorUuid));
            if (Utilities.listHasElements(rondas)) {
                return HttpResponse.ok(Utilities.parseList(rondas, RondaDTO.class));
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

    @Operation(summary = "Return a list of all Rounds for a given list of mentors")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Get("/getAllOfMentors")
    public HttpResponse<?> getAllOfMentors(@QueryValue List<String> mentorUuids) {
        try {
            List<Ronda> rondas = rondaService.getAllOfMentors(mentorUuids);
            if (Utilities.listHasElements(rondas)) {
                return HttpResponse.ok(Utilities.parseList(rondas, RondaDTO.class));
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

    @Operation(summary = "update Ronda")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Patch(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            value = "/update"
    )
    public HttpResponse<RestAPIResponse> patchRonda (@Body RondaDTO rondaDTO, Authentication authentication) {
        try {
            Ronda ronda = new Ronda(rondaDTO);


            Ronda r = this.rondaService.update(ronda, (Long) authentication.getAttributes().get("userInfo"));

            return HttpResponse.created(new RondaDTO(r));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }


    @Operation(summary = "delete Ronda")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Delete(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON,
            value = "/delete"
    )
    public HttpResponse<RestAPIResponse> delete (@QueryValue("uuid") String uuid, Authentication authentication) {
        try {
            this.rondaService.delete(uuid, (Long) authentication.getAttributes().get("userInfo"));

            return HttpResponse.ok();
        } catch (Exception e) {
            LOG.error(e.getMessage());
            return HttpResponse.badRequest().body(MentoringAPIError.builder()
                    .status(HttpStatus.BAD_REQUEST.getCode())
                    .error(e.getLocalizedMessage())
                    .message(e.getMessage()).build());
        }
    }

    @Operation(summary = "Generate Summary Report of Ronda")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Get("/report/{uuid}")
    public RondaReportDTO generateReport(@PathVariable("uuid") String uuid){
       return this.rondaService.generateReport(uuid);
    }
}
