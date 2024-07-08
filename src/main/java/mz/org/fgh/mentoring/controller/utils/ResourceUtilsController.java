package mz.org.fgh.mentoring.controller.utils;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.controller.tutor.TutorController;
import mz.org.fgh.mentoring.dto.mentorship.DoorDTO;
import mz.org.fgh.mentoring.dto.mentorship.IterationTypeDTO;
import mz.org.fgh.mentoring.dto.mentorship.TimeOfDayDTO;
import mz.org.fgh.mentoring.dto.question.QuestionTypeDTO;
import mz.org.fgh.mentoring.service.career.CareerTypeService;
import mz.org.fgh.mentoring.service.mentorship.DoorService;
import mz.org.fgh.mentoring.service.mentorship.IterationTypeService;
import mz.org.fgh.mentoring.service.mentorship.TimeOfDayService;
import mz.org.fgh.mentoring.service.question.QuestionTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.UTILS_CONTROLLER)
public class ResourceUtilsController extends BaseController {

    @Inject
    private CareerTypeService careeTypeService;

    @Inject
    private QuestionTypeService questionTypeService;

    @Inject
    private DoorService doorService;

    @Inject
    private IterationTypeService iterationTypeService;

    @Inject
    private TimeOfDayService timeOfDayService;

    public static final Logger LOG = LoggerFactory.getLogger(TutorController.class);

    /*
    @Operation(summary = "Return a list off all CareerType")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "CareerType")
    @Version(API_VERSION)
    @Get("/careertypes")
    public List<CareerType> findAllCareeType1(){
        LOG.debug("Searching CareerTypes version 1");
        return this.careeTypeService.findAllCareeType();
    }
     */

    @Operation(summary = "Return a list off all QuestionType")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "QuestionType")
    @Version(API_VERSION)
    @Get("/questiontypes")
    public List<QuestionTypeDTO> findAllQuestionType1(){
        LOG.debug("Searching QuestionType version 2");
        return this.questionTypeService.findAll();
    }

    /*
    @Get("/careertypes")
    public List<CareerType> findAllCareeType(){
        LOG.debug("Searching CareerTypes version 2");
        return this.careeTypeService.findAllCareeType();
    }

     */

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Return a list off all doors")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Door")
    @Get("/doors")
    public List<DoorDTO> findAllDoors(){
        LOG.debug("Searching Doors...");
        return this.doorService.findAll();
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Return a list off all iterations types")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "IterationType")
    @Get("/iterationTypes")
    public List<IterationTypeDTO> findAllIterationsTypes(){
        LOG.debug("Searching Iterations Types...");
        return this.iterationTypeService.findAll();
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Return a list off all times of day")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "TimeOfDay")
    @Get("/timesOfDay")
    public List<TimeOfDayDTO> findAllTimesOfDay(){
        LOG.debug("Searching Times of Day...");
        return this.timeOfDayService.findAll();
    }

}
