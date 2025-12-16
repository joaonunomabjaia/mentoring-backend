package mz.org.fgh.mentoring.dto.tutor;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Introspected
public class PasswordResetDTO {

    @NotBlank(message = "Token não pode estar vazio")
    private String token;

    @NotBlank(message = "Nova senha não pode estar vazia")
    @Size(min = 6, message = "A nova senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "Confirmação da senha não pode estar vazia")
    private String confirmPassword;

    public PasswordResetDTO() {}

    public PasswordResetDTO(String token, String password, String confirmPassword) {
        this.token = token;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
