package de.dlh.lhind.ecohack.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends LoginDto {

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;
}
