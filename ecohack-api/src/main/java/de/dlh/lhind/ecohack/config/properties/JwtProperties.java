package de.dlh.lhind.ecohack.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt.expiration.minutes")
@Getter
@Setter
public class JwtProperties {
    private Integer access;
    private Integer refresh;
}
