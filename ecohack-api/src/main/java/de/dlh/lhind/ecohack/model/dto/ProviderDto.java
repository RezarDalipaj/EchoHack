package de.dlh.lhind.ecohack.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private Double latitude;

    @NotNull
    private Double longtitude;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;
}
