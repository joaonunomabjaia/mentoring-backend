package mz.org.fgh.mentoring.dto.role;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class RoleDTO {

    private String description;

    private  String code;

    private  String level;

    private List<RoleAuthorityDTO> roleAuthorityDTOS;
}
