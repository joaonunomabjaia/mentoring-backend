package mz.org.fgh.mentoring.dto.authority;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mz.org.fgh.mentoring.base.BaseEntityDTO;
import mz.org.fgh.mentoring.entity.authority.Authority;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class AuthorityDTO extends BaseEntityDTO {
    private  String module;
    private String description;
    private  String code;

    public AuthorityDTO(Authority authority) {
        super(authority);
        this.module = authority.getModule();
        this.description = authority.getDescription();
        this.code = authority.getCode();
    }
}
