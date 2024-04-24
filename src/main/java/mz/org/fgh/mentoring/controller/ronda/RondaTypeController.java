package mz.org.fgh.mentoring.controller.ronda;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
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
import mz.org.fgh.mentoring.dto.ronda.RondaTypeDTO;
import mz.org.fgh.mentoring.entity.ronda.Ronda;
import mz.org.fgh.mentoring.entity.ronda.RondaType;
import mz.org.fgh.mentoring.service.ronda.RondaService;
import mz.org.fgh.mentoring.service.ronda.RondaTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.RONDA_TYPE)
public class RondaTypeController extends BaseController {

    @Inject
    private RondaTypeService rondaTypeService;

    public static final Logger LOG = LoggerFactory.getLogger(RondaTypeController.class);
    public RondaTypeController() {
    }

    @Operation(summary = "Return a list off all Round Types")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "RondaType")
    @Secured(SecurityRule.IS_ANONYMOUS)
    @Get("getall")
    public List<RondaTypeDTO> getAll() {
        List<RondaType> rondaTypes = rondaTypeService.findAll();
        List<RondaTypeDTO> rondaTypeDTOS = new ArrayList<>();
        for(RondaType rondaType : rondaTypes){
            rondaTypeDTOS.add(new RondaTypeDTO(rondaType));
        }
        return rondaTypeDTOS;
    }

}
