package mz.org.fgh.mentoring.dto.role;

import io.micronaut.core.annotation.Creator;
import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.authority.AuthorityDTO;
import mz.org.fgh.mentoring.entity.role.RoleAuthority;

@Data
@AllArgsConstructor
@Introspected
public class RoleAuthorityDTO extends BaseEntityDTO {

    private RoleDTO roleDTO;

    private AuthorityDTO authorityDTO;

    @Creator
    public RoleAuthorityDTO() {
    }

    public RoleAuthorityDTO(RoleAuthority roleAuthority) {
        super(roleAuthority);
        this.setAuthorityDTO(new AuthorityDTO(roleAuthority.getAuthority()));
    }
}
