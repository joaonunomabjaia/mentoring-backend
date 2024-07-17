package mz.org.fgh.mentoring.controller.user;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Patch;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.controller.program.ProgramController;
import mz.org.fgh.mentoring.dto.role.UserRoleDTO;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.service.user.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.USER_ROLE_CONTROLLER)
public class UserRoleController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(ProgramController.class);

    @Inject
    private UserRoleService userRoleService;
    @Operation(summary = "Return a list off all User Roles")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "UserRole")
    @Get("/getAll")
    public List<UserRoleDTO> getAll() {
        return userRoleService.findAllUserRoles();
    }
  
    @Operation(summary = "Update User from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Put("/merge")
    public HttpResponse<RestAPIResponse> merge (@QueryValue Long userId, @QueryValue Long roleId, Authentication authentication) {
        UserRole userRole = userRoleService.mergeUserRole(userId, roleId, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created User {}", userRole);
        return HttpResponse.ok().body(new UserRoleDTO(userRole));
    }
}
