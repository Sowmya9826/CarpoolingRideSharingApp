package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.respository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/", "/home"})
    public String userHome(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login"; // Redirect to login if not logged in
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        return "home";
    }
}
