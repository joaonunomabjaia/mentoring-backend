package mz.org.fgh.mentoring.dto.role;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.dto.location.LocationDTO;
import mz.org.fgh.mentoring.entity.location.Location;
import mz.org.fgh.mentoring.entity.role.Role;
import mz.org.fgh.mentoring.entity.role.RoleAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class RoleDTO extends BaseEntityDTO {

    private String description;

    private  String code;

    private  String level;

    private List<RoleAuthorityDTO> roleAuthorityDTOS;

    private  int hierarchyLevel;

    public RoleDTO(Role role) {
        super(role);
        this.description = role.getDescription();
        this.code = role.getCode();
        this.level = role.getLevel();
        this.setRoleAuthorityDTOS(setRoleAuth(role.getRoleAuthorities()));
        this.hierarchyLevel = role.getHierarchyLevel();
    }

    private List<RoleAuthorityDTO> setRoleAuth(List<RoleAuthority> roleAuthorities) {
        List<RoleAuthorityDTO> locationDTOSet = new ArrayList<>();

        for (RoleAuthority location : roleAuthorities) {
            locationDTOSet.add(new RoleAuthorityDTO(location));
        }
        return locationDTOSet;
    }
}
