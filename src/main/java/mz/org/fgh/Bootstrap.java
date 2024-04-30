package mz.org.fgh;

import io.micronaut.context.annotation.Requires;
import io.micronaut.context.env.Environment;
import io.micronaut.context.event.StartupEvent;
import io.micronaut.runtime.event.annotation.EventListener;
import jakarta.inject.Singleton;

import javax.transaction.Transactional;

@Singleton
@Requires(notEnv = Environment.TEST)
public class Bootstrap {


    @EventListener
    @Transactional
    void init(StartupEvent event) {
    }
}
