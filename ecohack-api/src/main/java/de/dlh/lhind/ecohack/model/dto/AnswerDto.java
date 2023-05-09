package de.dlh.lhind.ecohack.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerDto {
    private Integer number;
    private String answer;
    private Integer points;
}
