package mz.org.fgh.mentoring.controller.ronda;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
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
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.service.ronda.RondaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.RONDA)
public class RondaController extends BaseController {

    @Inject
    private RondaService rondaService;

    public static final Logger LOG = LoggerFactory.getLogger(RondaController.class);
    public RondaController() {
    }

    @Get("/{limit}/{offset}")
    public List<RondaDTO> getAll(@PathVariable("limit") long limit , @PathVariable("offset") long offset) {
        LOG.debug("Searching Ronda version 2");

        List<Ronda> rondas = new ArrayList<>();
        List<RondaDTO> rondaDTOs = new ArrayList<>();

        if(limit > 0){

            rondas = this.rondaService.findRondaWithLimit(limit,offset);
        } else {

            rondas = this.rondaService.findAll();
        }

        for(Ronda ronda : rondas){

            rondaDTOs.add(new RondaDTO());
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

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public HttpResponse<RestAPIResponse> create (@Body Ronda ronda) {


        LOG.debug("Created tutor {}", ronda);

        this.rondaService.createRonda(ronda);

        return HttpResponse.ok().body(ronda);
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
    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public List<RondaDTO> postRondas(@Body List<RondaDTO> rondaDTOS) {
        List<RondaDTO> dtos = this.rondaService.createRondas(rondaDTOS);
        return dtos;
    }

    @Operation(summary = "Save Ronda")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Ronda")
    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public HttpResponse<RondaDTO> postRonda (@Body RondaDTO rondaDTO) {

        RondaDTO dto = this.rondaService.createRonda(rondaDTO);

        return HttpResponse.ok().body(dto);
    }

}
