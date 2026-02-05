package ru.urfu.spring_urfu.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.urfu.spring_urfu.dto.UserDto;
import ru.urfu.spring_urfu.entity.Role;
import ru.urfu.spring_urfu.entity.User;
import ru.urfu.spring_urfu.repository.RoleRepository;
import ru.urfu.spring_urfu.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void SaveUser(UserDto user) {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }
        User new_user = new User();
        new_user.setName(user.getFirstName() + " " + user.getLastName());
        new_user.setEmail(user.getEmail());
        new_user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role role = roleRepository.findByName("ROLE_READ_ONLY");

        if (role == null) {
            role = createRoleIfNotExist("ROLE_READ_ONLY");
        }
        new_user.setRoles(List.of(role));
        userRepository.save(new_user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

//    private Role checkRoleExist() {
//        Role role = new Role();
//        role.setName("ROLE_ADMIN");
//        return roleRepository.save(role);
//    }

    private Role createRoleIfNotExist(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }

//    private UserDto mapToUserDto(User user) {
//        UserDto dto = new UserDto();
//        String[] name_parts = user.getName().split(" ");
//        dto.setFirstName(name_parts[0]);
//        dto.setLastName(name_parts.length > 1 ? name_parts[1] : "");
//        dto.setEmail(user.getEmail());
//        return dto;
//    }

    @Override
    @Transactional
    public void updateUserRoles(Long userId, List<String> roleNames) {
        // Находим пользователя
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Получаем текущие роли пользователя
        Set<Role> currentRoles = new HashSet<>(user.getRoles());

        // Получаем новые роли из репозитория
        List<Role> newRoles = new ArrayList<>();
        if (roleNames != null) {
            for (String roleName : roleNames) {
                Role role = roleRepository.findByName(roleName);
                if (role != null) {
                    newRoles.add(role);
                }
            }
        }

        // Обновляем роли
        currentRoles.clear();
        currentRoles.addAll(newRoles);
        user.setRoles(newRoles);
        userRepository.save(user);

    }
    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        String[] nameParts = user.getName().split(" ");
        dto.setFirstName(nameParts[0]);
        dto.setLastName(nameParts.length > 1 ? nameParts[1] : "");
        dto.setEmail(user.getEmail());
        dto.setId(user.getId());
        // Добавляем роли пользователя в DTO
        dto.setRoles(user.getRoles().stream().map(Role::getName).toList());
        return dto;
    }

    @Override
    public List<String> getAllRoleNames() {
        return roleRepository.findAll()
                .stream()
                .map(Role::getName)
                .toList();
    }


}
