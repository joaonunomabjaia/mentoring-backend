package mz.org.fgh;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import mz.org.fgh.mentoring.service.tutor.TutorService;

import javax.transaction.Transactional;

@Singleton
@Requires(notEnv = Environment.TEST)
public class Bootstrap {

    @Inject
    TutorService tutorService;

    @EventListener
    @Transactional
    void init(StartupEvent event) {
        tutorService.findAll();
    }
}
