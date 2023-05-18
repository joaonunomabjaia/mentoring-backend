package mz.org.fgh.mentoring.controller.utils;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.controller.tutor.TutorController;
import mz.org.fgh.mentoring.entity.career.CareerType;
import mz.org.fgh.mentoring.entity.form.FormType;
import mz.org.fgh.mentoring.entity.question.QuestionType;
import mz.org.fgh.mentoring.service.career.CareeTypeService;
import mz.org.fgh.mentoring.service.form.FormTypeService;
import mz.org.fgh.mentoring.service.question.QuestionTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.UTILS_CONTROLLER)
public class ResourceUtilsController extends BaseController {

    @Inject
    private CareeTypeService careeTypeService;
    @Inject
    private FormTypeService formTypeService;

    @Inject
    private QuestionTypeService questionTypeService;

    public static final Logger LOG = LoggerFactory.getLogger(TutorController.class);

    @Operation(summary = "Return a list off all CareerType")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "CareerType")
    @Version(API_VERSION)
    @Get("/careertypes")
    public List<CareerType> findAllCareeType1(){
        LOG.debug("Searching CareerTypes version 1");
        return this.careeTypeService.findAllCareeType();
    }

    @Operation(summary = "Return a list off all FormType")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "FormType")
    @Version(API_VERSION)
    @Get("/formtypes")
    public List<FormType> findAllFormType1(){
        LOG.debug("Searching FormType version 1");
        return this.formTypeService.findAll();
    }

    @Operation(summary = "Return a list off all QuestionType")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "QuestionType")
    @Version(API_VERSION)
    @Get("/questiontypes")
    public List<QuestionType> findAllQuestionType1(){
        LOG.debug("Searching QuestionType version 2");
        return this.questionTypeService.findAll();
    }

    @Get("/careertypes")
    public List<CareerType> findAllCareeType(){
        LOG.debug("Searching CareerTypes version 2");
        return this.careeTypeService.findAllCareeType();
    }

    @Get("/formtypes")
    public List<FormType> findAllFormType(){
        LOG.debug("Searching FormType version 2");
        return this.formTypeService.findAll();
    }

    @Get("/questiontypes")
    public List<QuestionType> findAllQuestionType(){
        LOG.debug("Searching QuestionType version 2");
        return this.questionTypeService.findAll();
    }

}
