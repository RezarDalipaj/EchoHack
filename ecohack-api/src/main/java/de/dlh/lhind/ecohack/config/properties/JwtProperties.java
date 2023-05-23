package de.dlh.lhind.ecohack.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {
    private Integer accessMinutes;
    private Integer refreshMinutes;
    private String accessSecret;
    private String refreshSecret;
}
