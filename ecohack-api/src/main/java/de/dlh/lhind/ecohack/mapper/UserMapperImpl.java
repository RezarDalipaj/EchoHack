package de.dlh.lhind.ecohack.mapper;

import de.dlh.lhind.ecohack.model.entity.User;
import de.dlh.lhind.ecohack.model.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole());
    }
}
