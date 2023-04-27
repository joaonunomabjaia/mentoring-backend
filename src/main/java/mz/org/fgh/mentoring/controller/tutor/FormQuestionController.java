package mz.org.fgh.mentoring.controller.tutor;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.entity.tutor.Tutor;
import mz.org.fgh.mentoring.service.formquestion.FormQuestionService;
import mz.org.fgh.mentoring.service.tutor.TutorService;
import mz.org.fgh.mentoring.service.tutored.TutoredService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Controller(RESTAPIMapping.FORM_QUESTION_CONTROLLER)
public class FormQuestionController extends BaseController {

    @Inject
    FormQuestionService formQuestionService;
    @Inject
    TutorService tutoreService;
    public FormQuestionController(){
    }

    public static final Logger LOG = LoggerFactory
            .getLogger(FormQuestionController.class);


    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "FormQuestion")
    @Version(API_VERSION)
    @Get
    public List<FormQuestion> getAll() {
        LOG.debug("Searching formQuestions version 2");
        return formQuestionService.findAll();
    }

    @Get
    public List<FormQuestion> getAllV1() {
        LOG.debug("Searching formQuestions version 1");
        return formQuestionService.findAll();
    }

    @Get
    public Optional<FormQuestion> findById(@Body Long id){
        return formQuestionService.findById(id);
    }

    @Get("{/form}")
    public List<FormQuestion> findFormQuestionByForm(@PathVariable("form") Long formId){

        if(formId != null) {
            LOG.debug("Searching form ,{form}", formId);
            Form formQuestion = new Form();
            formQuestion.setId(formId);
            return this.formQuestionService.fetchByForm(formQuestion);
        }
        Tutor tutor =  tutoreService.findById(formId).get();
        LOG.debug("Searching formQuestion ,{form}", tutor);
        return  this.formQuestionService.fetchByTutor(tutor);
    }

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public FormQuestion create (@Body FormQuestion formQuestion) {
        LOG.debug("Created formQuestion {} ", formQuestion);
        return this.formQuestionService.create(formQuestion);
    }
}
