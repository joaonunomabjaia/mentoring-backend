package mz.org.fgh.mentoring.controller.career;

import io.micronaut.core.annotation.Nullable;
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
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.career.CareerDTO;
import mz.org.fgh.mentoring.dto.career.CareerTypeDTO;
import mz.org.fgh.mentoring.entity.career.Career;
import mz.org.fgh.mentoring.service.career.CareerService;
import mz.org.fgh.mentoring.service.career.CareerTypeService;
import mz.org.fgh.mentoring.util.Utilities;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Controller(RESTAPIMapping.CAREER_CONTROLLER)
public class CareerController extends BaseController {

    @Inject
    CareerService careerService;
    @Inject
    private CareerTypeService careerTypeService;

    @Secured(SecurityRule.IS_ANONYMOUS)
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/getall")
    public List<CareerDTO> getAll(@Nullable @QueryValue("limit") Long limit ,
                                  @Nullable @QueryValue("offset") Long offset) {
        try{
        List<Career> careers = new ArrayList<>();
        if(limit!=null && offset!=null && limit > 0){
            careers = careerService.findCareerWithLimit(limit, offset);
        }else {
            careers = careerService.findAll();
        }
        return Utilities.parseList(careers, CareerDTO.class);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
              throw new RuntimeException(e);
          }
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Return a list off all Cabinets")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Cabinet")
    @Get("/careertypes/getall")
    public List<CareerTypeDTO> getCareerTypes(@Nullable @QueryValue("limit") Long limit ,
                                              @Nullable @QueryValue("offset") Long offset) {
        return this.careerTypeService.findAllCareerTypes(limit, offset);
    }

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public Career create (@Body Career career) {
        return this.careerService.create(career);
    }

}
