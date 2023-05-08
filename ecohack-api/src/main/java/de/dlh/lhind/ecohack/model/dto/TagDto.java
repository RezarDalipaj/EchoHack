package de.dlh.lhind.ecohack.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TagDto {
    private Long id;
    @NotBlank
    private String name;
}
