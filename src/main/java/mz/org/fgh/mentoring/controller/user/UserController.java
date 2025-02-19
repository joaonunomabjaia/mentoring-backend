package mz.org.fgh.mentoring.controller.user;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
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
import mz.org.fgh.mentoring.entity.role.Role;
import mz.org.fgh.mentoring.entity.role.UserRole;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.service.role.RoleService;
import mz.org.fgh.mentoring.service.user.UserRoleService;
import mz.org.fgh.mentoring.service.user.UserService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller(RESTAPIMapping.USER_CONTROLLER)
public class UserController extends BaseController {
    public static final Logger LOG = LoggerFactory.getLogger(ProgramController.class);

    @Inject
    private UserService userService;

    @Inject
    private RoleService roleService;
    @Inject
    private UserRoleService userRoleService;

    @Get("/getByCredencials/{username}/{password}")
    public UserDTO findByCredencials (@PathVariable("username") String username, @PathVariable("password") String password){
        return this.userService.getByCredentials(new User(username, password));
    }


    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Get("/getAll")
    public Page<UserDTO> getAll(Pageable pageable) {
        return userService.findAllUsers(pageable);
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

        for (Long roleId: userDTO.getRoleIds()){
            Optional<Role> optionalRole = roleService.findById(roleId);
            if(optionalRole.isPresent()){

//                UserRole createdUserRole = userRoleService.mergeUserRole(user.getId(),roleId, (Long) authentication.getAttributes().get("userInfo"));
                UserRole createdUserRole = userRoleService.create(user.getId(),roleId, (Long) authentication.getAttributes().get("userInfo"));
                user.getUserRoles().add(createdUserRole);
            }
        }

        LOG.info("Created User {}", user);

        return HttpResponse.ok().body(new UserDTO(user));
    }

    @Operation(summary = "Update User from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Patch("/update")
    public HttpResponse<RestAPIResponse> update (@Body UserDTO userDTO, Authentication authentication) {
        User userDB = this.userService.findById(userDTO.getId());
        User user = this.userService.update(userDTO,(Long) authentication.getAttributes().get("userInfo"));

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
        User user = this.userService.resetPassword(userDTO, userDB.getId());

        LOG.info("Created User {}", user);

        return HttpResponse.ok().body(new UserDTO(user));
    }

    @Operation(summary = "User Password Reset to database from mobile")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Patch("/password-update")
    public HttpResponse<RestAPIResponse> updatePassword (@Body UserDTO userDTO, Authentication authentication) {
        userService.updateUserPassword(new User(userDTO), false);

        LOG.info("updated User {}", userDTO);

        return HttpResponse.ok().body(userDTO);
    }

    @Operation(summary = "Batch User Password Reset from mobile")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Patch("/password-update-batch")
    public HttpResponse<RestAPIResponse> updatePasswords(@Body List<UserDTO> userDTOs, Authentication authentication) {
        try {
            userService.updateUserPasswords(Utilities.parseList(userDTOs, User.class), false);

            LOG.info("Updated passwords for {} users", userDTOs.size());

            return HttpResponse.ok();
        } catch (Exception e) {
            LOG.error("Error updating passwords: {}", e.getMessage(), e);
            return HttpResponse.badRequest();
        }
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

    @Operation(summary = "Get Users from database by UUID list")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Get("/getByUuids")
    public List<UserDTO> getByUuids(@QueryValue List<String> uuids) {
        List<User> users = this.userService.findByUuids(uuids);
        return users.stream().map(UserDTO::new).collect(Collectors.toList());
    }


    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Get("/search")
    public Page<UserDTO> searchUser(@NonNull @QueryValue("userId") Long userId,
                                    @Nullable @QueryValue("name") String name,
                                    @Nullable @QueryValue("nuit") String nuit,
                                    @Nullable @QueryValue("username") String userName,
                                    Pageable pageable){


        return  this.userService.searchUser(userId, name, nuit, userName, pageable);

    }
}
