package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.dto.TagDto;
import de.dlh.lhind.ecohack.model.entity.Tag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface TagMapper {

    TagDto toTagDto(Tag tag);
    List<TagDto> toTagDtoList(List<Tag> tags);
    Tag toTag(TagDto tagDto);
    List<Tag> toTagList(List<TagDto> tagDto);
}
