package de.dlh.lhind.ecohack.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class QuestionDto {
    private Integer questionNumber;
    private String question;
    private List<AnswerDto> answers;
}
