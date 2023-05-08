package de.dlh.lhind.ecohack.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto extends LoginDto {
    private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private Long latitude;
    @NotBlank
    private Long longtitude;
    private String paymentMethod;
    private Integer rankingPoints;
}
