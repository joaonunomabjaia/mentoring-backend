package mz.org.fgh.mentoring.dto.tutor;

import lombok.Data;

@Data
public class PasswordResetRequestDTO {
    private String email;
    private String channel;     // WEB, MOBILE
    private String deviceId;    // Se vier do mobile
}
