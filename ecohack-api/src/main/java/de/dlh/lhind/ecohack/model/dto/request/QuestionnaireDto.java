package de.dlh.lhind.ecohack.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestionnaireDto {
    private Integer questionOneResult;
    private Integer questionTwoResult;
    private Integer questionThreeResult;
    private Integer questionFourResult;
}
