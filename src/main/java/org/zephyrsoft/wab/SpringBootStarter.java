package org.zephyrsoft.wab;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class SpringBootStarter {
    public static void main(final String[] args) {
        SpringApplication.run(SpringBootStarter.class, args);
    }
}
