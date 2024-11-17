package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.UserDto;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.respository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/sign-up")
    public String showCreateUserForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "auth_pages/sign_up_form";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "auth_pages/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String emailId, @RequestParam String password, HttpSession session) {
        User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (user.getPassword().equals(password)) {
            // Initialize session
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFirstName());
            return "redirect:/home";
        }

        return "redirect:/auth/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/auth/login";
    }

}
