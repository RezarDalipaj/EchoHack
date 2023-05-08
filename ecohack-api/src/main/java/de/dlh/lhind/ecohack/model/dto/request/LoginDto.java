package de.dlh.lhind.ecohack.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginDto implements Serializable {

    @Schema(example = "user")
    @NotBlank
    @Email
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(example = "password")
    @NotBlank
    private String password;
}
