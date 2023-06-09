package de.dlh.lhind.ecohack.util.filter;

import de.dlh.lhind.ecohack.model.dto.FilterDto;
import de.dlh.lhind.ecohack.model.dto.KeyValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class FilterUtil<T> {

    public Specification<T> filterFieldWithEqualOperator(KeyValue keyValue) {
        return (root, query, builder) -> builder.equal(root.get(keyValue.getKey()), keyValue.getValue());
    }

    public Specification<T> filterFieldWithLikeOperator(KeyValue keyValue) {
        return (root, query, builder) -> builder.like(root.get(keyValue.getKey()), keyValue.getValue().toString());
    }

    public Specification<T> filterWithAndEqualOperators(FilterDto filterDto){
        var keyValues = filterDto.getInternalKeyValues();
        Specification<T> specification = Specification.where(null);
        for (var keyValue : keyValues){
            specification = specification.and(filterFieldWithEqualOperator(keyValue));
        }
        return specification;
    }

    public Specification<T> filterWithAndLikeOperators(FilterDto filterDto){
        var keyValues = filterDto.getInternalKeyValues();
        Specification<T> specification = Specification.where(null);
        for (var keyValue : keyValues){
            specification = specification.and(filterFieldWithLikeOperator(keyValue));
        }
        return specification;
    }

    public Specification<T> filterWithOrEqualOperators(FilterDto filterDto){
        var keyValues = filterDto.getInternalKeyValues();
        Specification<T> specification = Specification.where(null);
        for (var keyValue : keyValues){
            specification = specification.or(filterFieldWithEqualOperator(keyValue));
        }
        return specification;
    }
}
