package mz.org.fgh.mentoring.entity.earesource;
import io.micronaut.core.annotation.Creator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import mz.org.fgh.mentoring.base.BaseEntity;
import mz.org.fgh.mentoring.dto.resource.ResourceDTO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.File;

/**
 * @author Joao Nuno Mabjaia
 */

@Schema(name = "Resource", description = "A Teaching and learning resource")
@Entity(name = "Resource")
@Table(name = "resources")
@Data
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Resource extends BaseEntity {
    @Column(name = "RESOURCE", nullable = false)
    private String resource;

    @Creator
    public Resource(){}

    public Resource(ResourceDTO resourceDTO) {
        super(resourceDTO);
        this.setResource(resourceDTO.getResource());
    }

    @Override
    public String toString() {
        return "Resource{" +
                "resource='" + resource + '\'' +
                '}';
    }
}
