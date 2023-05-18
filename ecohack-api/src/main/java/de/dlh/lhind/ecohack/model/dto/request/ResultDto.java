package de.dlh.lhind.ecohack.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultDto {
    private Integer questionNumber;

    @NotNull
    private Integer result;
}
