package de.dlh.lhind.ecohack.util.filter;

import de.dlh.lhind.ecohack.model.dto.FilterDto;
import de.dlh.lhind.ecohack.model.dto.KeyValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class FilterUtil<T> {

    private Specification<T> filterEqualByField(KeyValue keyValue) {
        return (root, query, builder) -> builder.equal(root.get(keyValue.getKey()), keyValue.getValue());
    }

    private Specification<T> filterLikeField(KeyValue keyValue) {
        return (root, query, builder) -> builder.like(root.get(keyValue.getKey()), keyValue.getValue().toString());
    }

    public Specification<T> filterEqualAnd(FilterDto filterDto){
        var keyValues = filterDto.getInternalKeyValues();
        Specification<T> specification = Specification.where(null);
        for (var keyValue : keyValues){
            specification = specification.and(filterEqualByField(keyValue));
        }
        return specification;
    }

    public Specification<T> filterLikeAnd(FilterDto filterDto){
        var keyValues = filterDto.getInternalKeyValues();
        Specification<T> specification = Specification.where(null);
        for (var keyValue : keyValues){
            specification = specification.and(filterLikeField(keyValue));
        }
        return specification;
    }

    public Specification<T> filterOr(FilterDto filterDto){
        var keyValues = filterDto.getInternalKeyValues();
        Specification<T> specification = Specification.where(null);
        for (var keyValue : keyValues){
            specification = specification.or(filterEqualByField(keyValue));
        }
        return specification;
    }
}
