package mz.org.fgh.mentoring.controller.user;

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
import mz.org.fgh.mentoring.api.PaginatedResponse;
import mz.org.fgh.mentoring.api.RESTAPIMapping;
import mz.org.fgh.mentoring.api.RestAPIResponse;
import mz.org.fgh.mentoring.api.SuccessResponse;
import mz.org.fgh.mentoring.base.BaseController;
import mz.org.fgh.mentoring.controller.program.ProgramController;
import mz.org.fgh.mentoring.dto.LifeCycleStatusDTO;
import mz.org.fgh.mentoring.dto.tutor.PasswordResetDTO;
import mz.org.fgh.mentoring.dto.tutor.PasswordResetRequestDTO;
import mz.org.fgh.mentoring.dto.user.UserDTO;
import mz.org.fgh.mentoring.dto.user.UserPasswordDTO;
import mz.org.fgh.mentoring.entity.employee.Employee;
import mz.org.fgh.mentoring.entity.tutored.PasswordReset;
import mz.org.fgh.mentoring.entity.user.User;
import mz.org.fgh.mentoring.service.role.RoleService;
import mz.org.fgh.mentoring.service.tutor.PasswordResetService;
import mz.org.fgh.mentoring.service.user.UserRoleService;
import mz.org.fgh.mentoring.service.user.UserService;
import mz.org.fgh.mentoring.util.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

    @Inject
    private PasswordResetService passwordResetService;

    @Get("/getByCredencials/{username}/{password}")
    public UserDTO findByCredencials(@PathVariable("username") String username, @PathVariable("password") String password) {
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
    public UserDTO findUserById(@PathVariable("id") Long id) {

        User user = this.userService.findById(id);
        return new UserDTO(user);
    }

    @Operation(summary = "Update an existing User")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Put
    public HttpResponse<?> update(@Body UserDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        User user = new User(dto);
        user.setUpdatedBy(userUuid);
        User updated = this.userService.update(user);
        return HttpResponse.ok(SuccessResponse.of("Utilizador atualizado com sucesso", new UserDTO(updated)));
    }

    @Secured(SecurityRule.IS_ANONYMOUS)
    @Operation(summary = "Esqueceu a password - envia email de recuperação")
    @Post("/forgot-password")
    public HttpResponse<?> forgotPassword(@Body PasswordResetRequestDTO dto) {
        try {

            // Chama o serviço para gerar token e enviar email
            PasswordReset passwordReset = passwordResetService.generateAndSendPasswordResetToken(dto);

            return HttpResponse.ok(SuccessResponse.messageOnly(
                    "Email de recuperação enviado com sucesso para " + passwordReset.getEmail()
            ));
        } catch (IllegalArgumentException e) {
            return HttpResponse.badRequest(SuccessResponse.messageOnly(e.getMessage()));
        } catch (Exception e) {
            LOG.error("Erro ao enviar email de recuperação", e);
            return HttpResponse.serverError(SuccessResponse.messageOnly("Erro interno ao processar pedido"));
        }
    }

    @Operation(summary = "Activate or deactivate a User by changing its LifeCycleStatus")
    @Put("/{uuid}/status")
    public HttpResponse<?> updateLifeCycleStatus(@PathVariable String uuid,
                                                 @Body LifeCycleStatusDTO dto,
                                                 Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        User updated = userService.updateLifeCycleStatus(uuid, dto.getLifeCycleStatus(), userUuid);
        return HttpResponse.ok(
                SuccessResponse.of("Estado do utilizador atualizado com sucesso", new UserDTO(updated))
        );
    }

    @Operation(summary = "Update User password")
    @Put("/{uuid}/password")
    public HttpResponse<?> updatePassword(@PathVariable String uuid,
                                          @Body UserPasswordDTO dto,
                                          Authentication authentication) {
        String updatedByUuid = (String) authentication.getAttributes().get("useruuid");
        userService.updatePassword(uuid, dto.getNewPassword(), updatedByUuid);
        return HttpResponse.ok(
                SuccessResponse.messageOnly("Senha do utilizador atualizada com sucesso")
        );
    }

    @Operation(summary = "Delete a User by UUID")
    @Delete("/{uuid}")
    public HttpResponse<?> delete(@PathVariable String uuid) {
        userService.delete(uuid);
        return HttpResponse.ok(SuccessResponse.messageOnly("Utilizador eliminado com sucesso"));
    }

    @Operation(summary = "User Password Reset to database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Patch("/password-reset")
    public HttpResponse<RestAPIResponse> resetPassword(@Body UserDTO userDTO, Authentication authentication) {
        User userDB = this.userService.findById(userDTO.getId());
        User user = this.userService.resetPassword(userDTO, userDB.getId());

        LOG.info("Created User {}", user);

        return HttpResponse.ok().body(new UserDTO(user));
    }

    @Operation(summary = "User Password Reset with Token")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Patch("/password-reset-with-token")
    public HttpResponse<RestAPIResponse> resetPasswordWithToken(@Body PasswordResetDTO passwordResetDTO) {

        userService.resetPasswordWithToken(passwordResetDTO);

        return HttpResponse.created(SuccessResponse.of("Senha actualizada com sucesso!", true));
    }

    @Operation(summary = "User Password Reset to database from mobile")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Patch("/password-update")
    public HttpResponse<RestAPIResponse> updatePassword(@Body UserDTO userDTO, Authentication authentication) {
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
    public UserDTO destroyUser(@PathVariable("id") Long id, Authentication authentication) {

        User user = this.userService.findById(id);
        this.userService.destroy(user);

        return new UserDTO(user);
    }

    @Operation(summary = "Get User from database")
    @ApiResponse(content = @Content(mediaType = MediaType.APPLICATION_JSON))
    @Tag(name = "User")
    @Get("/getByuuid/{uuid}")
    public UserDTO getByuuid(@PathVariable("uuid") String uuid) {

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


    @Operation(summary = "Search users with filters (paginated)")
    @Get("/search")
    public HttpResponse<?> searchUser(@Nullable @QueryValue("query") String query,
                                      @Nullable Pageable pageable) {

        Page<UserDTO> data = userService.searchUser(query, resolvePageable(pageable));

        List<UserDTO> dataDTOs = data.getContent();

        String message = data.getTotalSize() == 0
                ? "Sem Dados para esta pesquisa"
                : "Dados encontrados";

        return HttpResponse.ok(
                PaginatedResponse.of(
                        dataDTOs,
                        data.getTotalSize(),
                        data.getPageable(),
                        message
                )
        );
    }

    @Operation(summary = "Create a new User")
    @Post
    public HttpResponse<?> create(@Body UserDTO dto, Authentication authentication) {
        String userUuid = (String) authentication.getAttributes().get("useruuid");
        User user = new User(dto);
        user.setCreatedBy(userUuid);
        User created = userService.create(user);
        return HttpResponse.created(SuccessResponse.of("Utilizador criado com sucesso", new UserDTO(created)));
    }

}

