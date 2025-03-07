package com.example.mobilestore.mapper;

import com.example.mobilestore.dto.RegisterDTO;
import com.example.mobilestore.dto.UserDTO;
import com.example.mobilestore.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    @Mapping(target = "role", source = "role.id")
    UserDTO toDTO(User user);

   // @Mapping(target = "role.id", source = "role")
   @Mapping(target = "role", expression = "java(new Role(userDTO.getRole()))")
    User toEntity(UserDTO userDTO);

}
