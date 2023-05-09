package de.dlh.lhind.ecohack.model.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Answers {
    QUESTION_ONE_1(3),
    QUESTION_ONE_2(2),
    QUESTION_ONE_3(5),
    QUESTION_TWO_1(1),
    QUESTION_TWO_2(5),
    QUESTION_TWO_3(4),
    QUESTION_THREE_1(5),
    QUESTION_THREE_2(2),
    QUESTION_THREE_3(3),
    QUESTION_FOUR_1(4),
    QUESTION_FOUR_2(2),
    QUESTION_FOUR_3(4);

    private final Integer points;

}
