package de.dlh.lhind.ecohack.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionnaireDto {
    @NotNull
    private Integer questionOneResult;
    @NotNull
    private Integer questionTwoResult;
    @NotNull
    private Integer questionThreeResult;
    @NotNull
    private Integer questionFourResult;
}
