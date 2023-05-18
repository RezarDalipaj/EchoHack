package de.dlh.lhind.ecohack.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LogoutDto {
    private String logoutMessage;
}
