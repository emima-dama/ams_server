package ro.ubbcluj.cs.ams.attendance.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.cloud.config.environment.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeterConfiguration {

//    @Autowired
//    Environment environment;
//
//    String port = environment.getProperty("local.server.port");

    @Bean
    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {

        //return registry -> registry.config().namingConvention(MY_CUSTOM_CONVENTION);
        return registry -> registry.config().commonTags("appName","attendance-service").commonTags("namespace", "attendance").commonTags("pod", "localhost:8080") ;
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
