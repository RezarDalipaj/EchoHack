package de.dlh.lhind.ecohack.model.dto;

import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends LoginDto {
    private String role;
}
