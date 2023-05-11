package de.dlh.lhind.ecohack.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
public class QuizDto {
    private List<QuestionDto> questions;
}
