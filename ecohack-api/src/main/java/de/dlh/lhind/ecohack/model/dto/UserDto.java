package de.dlh.lhind.ecohack.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private String username;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;
}
