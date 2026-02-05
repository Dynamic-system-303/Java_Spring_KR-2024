package ru.urfu.spring_urfu.service;

import ru.urfu.spring_urfu.dto.UserDto;
import ru.urfu.spring_urfu.entity.User;

import java.util.List;

public interface UserService {
    void SaveUser(UserDto user);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
    void updateUserRoles(Long userId, List<String> roles);
    User findUserById(Long id);
    UserDto mapToUserDto(User user);
    List<String> getAllRoleNames();
}
