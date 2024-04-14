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
import mz.org.fgh.mentoring.dto.question.EvaluationTypeDTO;
import mz.org.fgh.mentoring.service.question.EvaluationTypeService;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.EVALUATION_TYPE)
public class EvaluationTypeController extends BaseController {

    @Inject
    private EvaluationTypeService evaluationTypeService;

    public EvaluationTypeController() {
    }

    @Operation(summary = "Return a list off all Evaluation Types")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "EvaluationType")
    @Get("/getAll")
    public List<EvaluationTypeDTO> getAllEvaluationTypes() {
        return evaluationTypeService.findAll();
    }
}
