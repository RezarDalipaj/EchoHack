package de.dlh.lhind.ecohack.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class FilterDto {

    @NotNull
    private List<KeyValue> internalKeyValues;

    @NotNull
    private List<KeyValue> externalKeyValues;
}
