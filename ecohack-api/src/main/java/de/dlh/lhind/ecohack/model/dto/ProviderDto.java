package de.dlh.lhind.ecohack.model.dto;

import de.dlh.lhind.ecohack.model.dto.request.LoginDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProviderDto extends LoginDto {
    private Long id;
    private String name;

    private String nipt;

    private Long latitude;

    private Long longtitude;
}
