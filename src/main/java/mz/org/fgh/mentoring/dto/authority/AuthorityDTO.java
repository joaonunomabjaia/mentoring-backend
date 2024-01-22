package mz.org.fgh.mentoring.dto.authority;

import io.micronaut.core.annotation.Introspected;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Introspected
public class AuthorityDTO {

    private  String module;

    private String description;

    private  String code;

}
