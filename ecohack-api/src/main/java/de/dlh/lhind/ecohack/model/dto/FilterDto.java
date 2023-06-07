package de.dlh.lhind.ecohack.model.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class FilterDto {

    @NotEmpty
    private List<KeyValue> keyValues;
}
