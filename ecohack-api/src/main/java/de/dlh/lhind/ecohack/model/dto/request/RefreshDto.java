package de.dlh.lhind.ecohack.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefreshDto {
    @NotBlank
    private String refreshToken;
}
