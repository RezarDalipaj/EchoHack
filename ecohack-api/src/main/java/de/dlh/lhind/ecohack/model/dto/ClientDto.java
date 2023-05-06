package de.dlh.lhind.ecohack.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDto extends LoginDto {
    private Long id;
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String role;
    private String name;
    private String surname;
    private Long latitude;
    private Long longtitude;
    private String paymentMethod;
    private Integer rankingPoints;
}
