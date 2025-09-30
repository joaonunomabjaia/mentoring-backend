package mz.org.fgh.mentoring.dto;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;
import mz.org.fgh.mentoring.util.LifeCycleStatus;

@Getter
@Setter
@Introspected
public class LifeCycleStatusDTO {

    @javax.validation.constraints.NotNull
    private LifeCycleStatus lifeCycleStatus;
}
