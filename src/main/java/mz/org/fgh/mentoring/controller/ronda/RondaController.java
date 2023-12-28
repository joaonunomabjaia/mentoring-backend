package mz.org.fgh.mentoring.controller.ronda;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
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

}
