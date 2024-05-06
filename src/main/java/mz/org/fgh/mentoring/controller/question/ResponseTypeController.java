package mz.org.fgh.mentoring.controller.question;

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
import mz.org.fgh.mentoring.dto.question.QuestionCategoryDTO;
import mz.org.fgh.mentoring.dto.question.ResponseTypeDTO;
import mz.org.fgh.mentoring.service.question.QuestionCategoryService;
import mz.org.fgh.mentoring.service.question.ResponseTypeService;

import java.util.List;

@Controller(RESTAPIMapping.RESPONSE_TYPE)
public class ResponseTypeController extends BaseController {

    @Inject
    private ResponseTypeService responseTypeService;

    public ResponseTypeController() {
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Return a list off all Response Types")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "ResponseType")
    @Get("/getAll")
    public List<ResponseTypeDTO> getAllResponseTypes() {
        return responseTypeService.findAll();
    }
}
