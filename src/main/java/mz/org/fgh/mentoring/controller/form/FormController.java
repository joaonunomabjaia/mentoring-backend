package mz.org.fgh.mentoring.controller.form;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.form.FormDTO;
import mz.org.fgh.mentoring.entity.form.Form;
import mz.org.fgh.mentoring.service.form.FormService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_AUTHENTICATED)
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

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Get("/getformsbycodeornameorprogrammaticarea")
    public List<FormDTO> getFormsByCodeAndNameAndProgrammaticAreaName(
            @NonNull @QueryValue("userId") Long userId,
            @Nullable @QueryValue("code") String code,
            @Nullable @QueryValue("name") String name,
            @Nullable @QueryValue("programmaticArea") String programmaticArea
    ) {
        List<FormDTO> forms = new ArrayList<>(0);
        forms = formService.search(userId, code, name, programmaticArea);;
        return forms;
    }

}
