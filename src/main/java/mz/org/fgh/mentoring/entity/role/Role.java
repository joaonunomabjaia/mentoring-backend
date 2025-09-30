package mz.org.fgh.mentoring.entity.role;

import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.role.RoleDTO;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(name = "Role", description = "Represent the role a user can be granted")
@Entity(name = "Role")
@Table(name = "role")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Role extends BaseEntity {

    @NotEmpty
    @Column(name = "description", nullable = false)
    private String description;

    @NotEmpty
    @Column(name = "code", nullable = false)
    private  String code;

    @NotEmpty
    @Column(name = "level", nullable = false)
    private  String level;

    @NotEmpty
    @Column(name = "HIERARCHY_LEVEL", nullable = false)
    private  int hierarchyLevel;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "role")
    private List<RoleAuthority> roleAuthorities;

    @Creator
    public Role() {
    }

    public Role(RoleDTO dto) {
        super(dto); // se BaseEntity possui um construtor com DTO
        this.description = dto.getDescription();
        this.code = dto.getCode();
        this.level = dto.getLevel();
        this.hierarchyLevel = dto.getHierarchyLevel();
    }

    @Override
    public String toString() {
        return "Role{" +
                "description='" + description + '\'' +
                ", code='" + code + '\'' +
                ", level='" + level + '\'' +
                ", hierarchyLevel=" + hierarchyLevel +
                ", roleAuthorities=" + roleAuthorities +
                '}';
    }
}
