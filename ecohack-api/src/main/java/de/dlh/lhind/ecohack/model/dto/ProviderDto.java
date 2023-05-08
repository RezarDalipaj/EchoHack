package de.dlh.lhind.ecohack.model.dto;

import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderDto extends LoginDto {
    private Long id;
    @NotBlank
    private String name;

    @NotBlank
    private String nipt;

    @NotBlank
    private Long latitude;

    @NotBlank
    private Long longtitude;
}
