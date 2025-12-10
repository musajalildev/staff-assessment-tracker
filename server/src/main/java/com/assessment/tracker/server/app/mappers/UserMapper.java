package com.assessment.tracker.server.app.mappers;

import com.assessment.tracker.server.api.dto.UserDTO;
import com.assessment.tracker.server.persistence.entities.User;
import com.assessment.tracker.server.utils.Mapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserDTO, User> {

    @Override
    public UserDTO entityToApi(User user) { // change entity to dto
        if (user == null)
            return null;

        UserDTO dto = new UserDTO();
        dto.setUserID(user.getUserID());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setUserType(user.getUserType());

        // password intentionally excluded from DTO
        return dto;
    }

    @Override
    public User apiToEntity(UserDTO dto) { // change dto to entity
        if (dto == null)
            return null;

        User user = new User();

        user.setUserID(dto.getUserID());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setUserType(dto.getUserType());

        // dto.getPassword() may be used for signup/creation
        if (dto.getPassword() != null) {
            user.setPassword(dto.getPassword());
        }

        return user;
    }
}
