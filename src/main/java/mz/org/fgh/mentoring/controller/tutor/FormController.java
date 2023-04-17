package mz.org.fgh.mentoring.controller.tutor;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
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
    @Tag(name = "Form")
    @Version(API_VERSION)
    @Get
    public List<Form> getAll() {
        LOG.debug("Searching forms version 2");
        return formService.findAll();
    }

    @Get
    public List<Form> getAllV1() {
        LOG.debug("Searching forms version 1");
        return formService.findAll();
    }

    @Get
    public Optional<Form> findById(@Body Long id){
        return formService.findById(id);
    }

    @Get
    public Form findByCode(@Body String code){
        return formService.findByCode(code);
    }

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public Form create (@Body Form form) {
        LOG.debug("Created form {} ", form);
        return this.formService.create(form);
    }

}
