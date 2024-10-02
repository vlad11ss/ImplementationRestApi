package com.example.RestApiExercise1.controllers;

import com.example.RestApiExercise1.models.User;
import com.example.RestApiExercise1.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @GetMapping("/home")
    public String getProfile(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        model.addAttribute("userName", user.getName());
        return "home";
    }


    @PostMapping("/registration")
    public String createUser(User user) {
        userService.createUser(user);
        return "redirect:/login";
    }
}
