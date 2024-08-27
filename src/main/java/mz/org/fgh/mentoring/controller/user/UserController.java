package mz.org.fgh.mentoring.controller.user;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
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
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.controller.program.ProgramController;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.service.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(RESTAPIMapping.USER_CONTROLLER)
public class UserController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(ProgramController.class);

    @Inject
    private UserService userService;

    @Get("/getByCredencials/{username}/{password}")
    public UserDTO findByCredencials (@PathVariable("username") String username, @PathVariable("password") String password){
        return this.userService.getByCredencials(new User(username, password));
    }

    @Operation(summary = "Return a list off all Users")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Get("/getAll")
    public List<UserDTO> getAll() {
        return userService.findAllUsers();
    }

    @Operation(summary = "Get User from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Get("/{id}")
    public UserDTO findUserById(@PathVariable("id") Long id){

        User user = this.userService.findById(id);
        return new UserDTO(user);
    }
    @Operation(summary = "Save User to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Post("/save")
    public HttpResponse<RestAPIResponse> create (@Body UserDTO userDTO, Authentication authentication) {

        User user = new User(userDTO);
        user = this.userService.create(user, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created User {}", user);

        return HttpResponse.ok().body(new UserDTO(user));
    }

    @Operation(summary = "Update User from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Patch("/update")
    public HttpResponse<RestAPIResponse> update (@Body UserDTO userDTO, Authentication authentication) {
        User userDB = this.userService.findById(userDTO.getId());
        User user = this.userService.update(userDTO, userDB, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created User {}", user);

        return HttpResponse.ok().body(new UserDTO(user));
    }

    @Operation(summary = "Delete User from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Patch("/{id}")
    public UserDTO deleteUser(@PathVariable("id") Long id, Authentication authentication){

        User user = this.userService.findById(id);        
        user = this.userService.delete(user, (Long) authentication.getAttributes().get("userInfo"));       

        return new UserDTO(user);
    }

    @Operation(summary = "User Password Reset to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Patch("/password-reset")
    public HttpResponse<RestAPIResponse> resetPassword (@Body UserDTO userDTO, Authentication authentication) {
        User userDB = this.userService.findById(userDTO.getId());
        User user = this.userService.resetPassword(userDTO, userDB, (Long) authentication.getAttributes().get("userInfo"));

        LOG.info("Created User {}", user);

        return HttpResponse.ok().body(new UserDTO(user));
    }

    @Operation(summary = "Destroy User from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Delete("/{id}")
    public UserDTO destroyUser(@PathVariable("id") Long id, Authentication authentication){

        User user = this.userService.findById(id);        
        this.userService.destroy(user);       

        return new UserDTO(user);
    }

    @Operation(summary = "Get User from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Get("/getByuuid/{uuid}")
    public UserDTO getByuuid(@PathVariable("uuid") String uuid){

        User user = this.userService.findByUuid(uuid);
        return new UserDTO(user);
    }

    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Get("/search")
    public List<UserDTO> searchUser(@NonNull @QueryValue("userId") Long userId,
                                    @Nullable @QueryValue("name") String name,
                                    @Nullable @QueryValue("nuit") Long nuit,
                                    @Nullable @QueryValue("username") String userName){

        List<UserDTO> userDTOS = new ArrayList<>();

        userDTOS = this.userService.searchUser(userId, name, nuit, userName);

        return  userDTOS;

    }
}
