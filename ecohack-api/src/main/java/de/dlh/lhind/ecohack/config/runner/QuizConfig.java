package de.dlh.lhind.ecohack.config.runner;

import de.dlh.lhind.ecohack.model.dto.AnswerDto;
import de.dlh.lhind.ecohack.model.dto.QuestionDto;
import de.dlh.lhind.ecohack.model.dto.QuizDto;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class QuizConfig {

    private final QuizDto quizDto;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent() {
        quizDto.setQuestions(createQuestions());
    }

    private List<QuestionDto> createQuestions(){
        var answers = List.of(createAnswer("Answer 1", 2, 1),
                createAnswer("Answer 2", 5, 2),
                createAnswer("Answer 3", 1, 3),
                createAnswer("Answer 4", 4, 4),
                createAnswer("Answer 5", 3, 5));
        return List.of(createQuestion(1, "Question 1", answers),
                createQuestion(2, "Question 2", answers),
                createQuestion(3, "Question 3", answers),
                createQuestion(4, "Question 4", answers));
    }

    private AnswerDto createAnswer(String answer, Integer points, Integer number){
        return AnswerDto.builder()
                .answer(answer)
                .points(points)
                .number(number)
                .build();
    }

    private QuestionDto createQuestion(Integer number, String question, List<AnswerDto> answers){
        return QuestionDto.builder()
                .questionNumber(number)
                .question(question)
                .answers(answers)
                .build();
    }
}
