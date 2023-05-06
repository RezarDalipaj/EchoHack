package de.dlh.lhind.ecohack.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@ConfigurationProperties(prefix = "jwt")
public class JwtSecret {
    private String secret;
}
