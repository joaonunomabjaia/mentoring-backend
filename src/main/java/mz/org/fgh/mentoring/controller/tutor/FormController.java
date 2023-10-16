package mz.org.fgh.mentoring.controller.tutor;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.tutor.FormDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.service.form.FormService;
import mz.org.fgh.mentoring.service.formquestion.FormQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.FORM_CONTROLLER)
public class FormController extends BaseController {

    @Inject
    FormService formService;
   public FormController(){
    }

    public static final Logger LOG = LoggerFactory
            .getLogger(FormController.class);

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/form/{limit}/{offset}")
    public List<FormDTO> getAll(@PathVariable("limit") long limit, @PathVariable("offset") long offset) {
        LOG.debug("Searching forms version 2");
        return formService.findAll(limit, offset);
    }

    @Get("/{id}")
    public Optional<Form> findById(@PathVariable("id") Long id){
        return formService.findById(id);
    }

    @Get("/sample-forms")
    public List<FormDTO> findSampleIndicatorForms(){
      return  this.formService.findSampleIndicatorForms();
    }

    @Get( consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON)
    public List<FormDTO> findBySelectedFilter(String code, String name, String programaticAreaCode, String partnerUUID ){
        return this.formService.findBySelectedFilter(code, name, programaticAreaCode, partnerUUID);
    }

    @Get("/programaticarea/{progrArea}")
    public List<FormDTO> findFormByProgrammaticAreaUuid(@PathVariable("progrArea") String progrArea){

        return this.formService.findFormByProgrammaticAreaUuid(progrArea);
    }

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public Form create (@Body Form form) {
        LOG.debug("Created form {} ", form);
        return this.formService.create(form);
    }

    @Put(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public Form update(@Body Form form){
        LOG.debug("update form {} ", form);
        return this.formService.update(form);
    }

}
