package com.outsider.paymybuddy.dto;

import com.outsider.paymybuddy.model.User;

import java.util.Set;

@org.mapstruct.Mapper
public interface MyMapper {

    UserDto userToUserDto(User user);
    User userDtoToUser(UserDto userDto);
    Set<UserDto> setUserToSetUserDto(Set<User> users);

}
