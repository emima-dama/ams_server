package ro.ubbcluj.cs.ams.auth.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeterConfiguration {

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {

        //return registry -> registry.config().namingConvention(MY_CUSTOM_CONVENTION);
        return registry -> registry.config().commonTags("appName","auth-service").commonTags("namespace", "authentication").commonTags("pod", "localhost:8080") ;
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
