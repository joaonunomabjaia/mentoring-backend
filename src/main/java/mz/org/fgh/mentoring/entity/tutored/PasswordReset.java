package mz.org.fgh.mentoring.entity.tutored;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "password_reset")
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PasswordReset extends BaseEntity {

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token;

    @Column(name = "EXPIRES_AT", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiresAt;

    @Column(name = "USED", nullable = false)
    private boolean used = false;

    @Column(name = "CHANNEL", length = 20) // WEB / MOBILE
    private String channel;

    @Column(name = "CLIENT_DEVICE_ID", length = 100)
    private String deviceId; // Mobile offline-friendly

    public boolean isExpired() {
        return new Date().after(this.expiresAt);
    }
}
