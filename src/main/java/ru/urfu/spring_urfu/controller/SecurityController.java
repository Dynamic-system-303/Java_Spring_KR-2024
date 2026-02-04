package ru.urfu.spring_urfu.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.urfu.spring_urfu.dto.UserDto;
import ru.urfu.spring_urfu.entity.User;
import ru.urfu.spring_urfu.service.UserService;

import java.util.List;

@Controller
public class SecurityController {
    private UserService userService;
    public SecurityController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/index")
    public String home() { return "index"; }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto dto = new UserDto();
        model.addAttribute("user", dto);
        return "register";
    }

    @GetMapping("/users")
    public String users(Model model) {
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "users";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto dto, BindingResult result, Model model) {
        User existingUser = userService.findUserByEmail(dto.getEmail());
        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", "На этот адрес уже зарегистрирована учетная запись");
        }
        if (result.hasErrors()) {
            model.addAttribute("user", dto);
            return "/register";
        }

        userService.SaveUser(dto);
        return "redirect:/register?success";
    }
}
