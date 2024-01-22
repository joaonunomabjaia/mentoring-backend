package mz.org.fgh.mentoring.dto.role;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.dto.authority.AuthorityDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class RoleAuthorityDTO {

    private RoleDTO roleDTO;

    private AuthorityDTO authorityDTO;
}
