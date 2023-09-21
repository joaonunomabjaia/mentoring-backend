package mz.org.fgh.mentoring.controller.career;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
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

import java.util.ArrayList;
import java.util.List;

@Controller(RESTAPIMapping.CAREER_CONTROLLER)
public class CareerController extends BaseController {

    @Inject
    CareerService careerService;
    @Inject
    private CareerTypeService careerTypeService;

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/career/{limit}/{offset}")
    public List<CareerDTO> getAll(@PathVariable("limit") long limit , @PathVariable("offset") long offset) {

        List<Career> careers = new ArrayList<>();

        if(limit > 0){
            careers = careerService.findCareerWithLimit(limit, offset);
        }else {
            careers = careerService.findAll();
        }
        List<CareerDTO> careerDTOS = new ArrayList<>(careers.size());
        for (Career career: careers) {
            CareerDTO careerDTO = new CareerDTO(career);
            careerDTOS.add(careerDTO);
        }
        return careerDTOS;
    }

    @Operation(summary = "Return a list off all Cabinets")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Cabinet")
    @Get("/career-types/{limit}/{offset}")
    public List<CareerTypeDTO> getCareerTypes(@PathVariable("limit") long limit , @PathVariable("offset") long offset) {
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
