package org.zephyrsoft.wab.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "wab.report")
public class ReportConfiguration {
    private String dateFormat;
    private String title;
    private String css;
    private String header;
    private String family;
    private String person;

}
