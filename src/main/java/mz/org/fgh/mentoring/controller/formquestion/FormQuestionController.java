package mz.org.fgh.mentoring.controller.formquestion;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.form.FormQuestionDTO;
import mz.org.fgh.mentoring.entity.formQuestion.FormQuestion;
import mz.org.fgh.mentoring.service.form.FormQuestionService;
import mz.org.fgh.mentoring.service.tutor.TutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

import static mz.org.fgh.mentoring.api.RESTAPIMapping.API_VERSION;

@Secured(SecurityRule.IS_AUTHENTICATED)
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

    @Operation(summary = "Return a list of forms for the listed params")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/getByFormId")
    @Tag(name = "FormQuestion")
    public List<FormQuestionDTO> findFormQuestionByFormId(@NonNull @QueryValue("formId") Long formId){
            LOG.debug("Searching form ,{form}", formId);
            List<FormQuestionDTO> dtos = this.formQuestionService.fetchByForm(formId);
            return dtos;
    }

    @Post(
            consumes = MediaType.APPLICATION_JSON,
            produces = MediaType.APPLICATION_JSON
    )
    public FormQuestion create (@Body FormQuestion formQuestion) {
        LOG.debug("Created formQuestion {} ", formQuestion);
        return this.formQuestionService.create(formQuestion);
    }

    @Operation(summary = "Remove a formQuestion")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "FormQuestion")
    @Patch("/remove")
    public void remove (@NonNull @QueryValue("formId") Long formId, @Body FormQuestionDTO formQuestionDTO, Authentication authentication) {
        LOG.debug("Removing a formQuestion {} ");
        this.formQuestionService.inactivate((Long) authentication.getAttributes().get("userInfo"), formId, formQuestionDTO);
    }
}
