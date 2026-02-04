package ru.urfu.spring_urfu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.urfu.spring_urfu.dto.UserDto;
import ru.urfu.spring_urfu.entity.Role;
import ru.urfu.spring_urfu.entity.User;
import ru.urfu.spring_urfu.repository.RoleRepository;
import ru.urfu.spring_urfu.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void SaveUser(UserDto user) {
        User new_user = new User();
        new_user.setName(user.getFirstName() + " " + user.getLastName());
        new_user.setEmail(user.getEmail());
        new_user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = checkRoleExist();
        }
        new_user.setRoles(List.of(new Role[]{role}));
        userRepository.save(new_user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map(this::mapToUserDto).collect(Collectors.toList());
    }

    private Role checkRoleExist() {
        Role role = new Role();
        role.setName("ROLE_ADMIN");
        return roleRepository.save(role);
    }

    private UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        String[] name_parts = user.getName().split(" ");
        dto.setFirstName(name_parts[0]);
        dto.setLastName(name_parts[1]);
        dto.setEmail(user.getEmail());
        return dto;
    }
}
