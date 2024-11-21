package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.UserDto;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.repository.UserRepository;
import com.carpoolapp.carpoolService.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public String createUser(UserDto userDto, @RequestParam("profileImage") MultipartFile file, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            User user = userService.registerUser(userDto, file);
            userService.initializeUserSession(session, user);
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/users/create?error=true";
        }

        return "redirect:/home";
    }


    @GetMapping("/profile")
    public String showUserProfile(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        userService.addProfileImageToUIModel(user, model);

        return "user_pages/profile";
    }


    @PostMapping("/update")
    public String updateUserProfile(
            @ModelAttribute UserDto userDto,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/auth/login";
        }

        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userService.updateUser(existingUser, userDto, profileImage);
        redirectAttributes.addFlashAttribute("message", "Profile updated successfully!");

        return "redirect:/users/profile";
    }

}