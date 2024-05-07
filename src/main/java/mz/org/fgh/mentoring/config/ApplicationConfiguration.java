package mz.org.fgh.mentoring.config;

import io.micronaut.core.annotation.Introspected;

import javax.persistence.Entity;

@Introspected(packages = {"mz.org.fgh.mentoring.entity"}, includedAnnotations = Entity.class)

public class ApplicationConfiguration {
}
