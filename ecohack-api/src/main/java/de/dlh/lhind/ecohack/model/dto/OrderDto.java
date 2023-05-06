package de.dlh.lhind.ecohack.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDto {
    private Long id;
    private LocalDateTime orderDate;
    private String comment;
    private Long clientId;
    private Long foodProviderId;
    private List<MealDto> meals;
}
