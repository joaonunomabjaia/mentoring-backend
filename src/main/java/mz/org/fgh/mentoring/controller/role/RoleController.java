package mz.org.fgh.mentoring.controller.role;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.role.RoleDTO;
import mz.org.fgh.mentoring.service.role.RoleService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.ROLES_CONTROLLER)
public class RoleController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(RoleController.class);

    private final  RoleService roleService;

    public RoleController(RoleService roleService){
        this.roleService=roleService;
    }

    @Operation(summary = "Return a list off all Roles")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "Role")
    @Get("/getAll")
    public List<RoleDTO> getAll() {
        return roleService.findAllRoles();
    }
}
