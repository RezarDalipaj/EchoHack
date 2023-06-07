package de.dlh.lhind.ecohack.util.filter;

import de.dlh.lhind.ecohack.model.dto.FilterDto;
import de.dlh.lhind.ecohack.model.dto.KeyValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

@Slf4j
public class FilterUtil<T> {

    public Specification<T> filterByField(KeyValue keyValue) {
        return (root, query, builder) -> builder.equal(root.get(keyValue.getKey()), keyValue.getValue());
    }

    public Specification<T> filter(FilterDto filterDto){
        var keyValues = filterDto.getKeyValues();
        Specification<T> specification = Specification.where(null);
        for (var keyValue : keyValues){
            specification = specification.and(filterByField(keyValue));
        }
        return specification;
    }
}
