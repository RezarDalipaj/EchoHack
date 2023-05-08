package de.dlh.lhind.ecohack.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionnaireDto {
    @NotBlank
    private Integer questionOneResult;
    @NotBlank
    private Integer questionTwoResult;
    @NotBlank
    private Integer questionThreeResult;
    @NotBlank
    private Integer questionFourResult;
}
