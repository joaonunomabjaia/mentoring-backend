package mz.org.fgh.mentoring.controller.user;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.service.user.UserService;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.USER_CONTROLLER)
public class UserController extends BaseController {

    @Inject
    private UserService userService;

    @Get("/getByCredencials/{username}/{password}")
    public UserDTO findByCredencials (@PathVariable("username") String username, @PathVariable("password") String password){
        return this.userService.getByCredencials(new User(username, password));
    }

}
