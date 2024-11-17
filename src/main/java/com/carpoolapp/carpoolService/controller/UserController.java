package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.UserDto;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.respository.UserRepository;
import com.carpoolapp.carpoolService.service.UserRegistrationService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRegistrationService userRegistrationService;


    @GetMapping("/")
    public String index() {
        return "sign_up_form";
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String emailId, @RequestParam String password, HttpSession session) {
        User user = userRepository.findByEmailId(emailId)
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (user.getPassword().equals(password)) {
            // Initialize session
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFirstName());
            return "redirect:/users/home";
        }

        return "redirect:/users/login?error=true";
    }

    @GetMapping("/home")
    public String userHome(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/users/login"; // Redirect to login if not logged in
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("user", user);
        return "user-home";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            UserDto userDto = new UserDto();
            userDto.setFirstName(user.get().getFirstName());
            userDto.setLastName(user.get().getLastName());
            userDto.setEmailId(user.get().getEmailId());
            userDto.setPhoneNumber(user.get().getPhoneNumber());
            userDto.setAge(user.get().getAge());
//            userDto.setDob(user.get().getDob());
            userDto.setPassword(user.get().getPassword());
            //userDto.setProfileImage(user.get().getProfileImage());
            return ResponseEntity.ok(userDto);
        }
    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        return "sign_up_form";
    }

    @PostMapping("/create")
    public String createUser(UserDto userDto, @RequestParam("profileImage") MultipartFile file, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            User user = userRegistrationService.registerUser(userDto, file);
            redirectAttributes.addFlashAttribute("user", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFirstName());
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/users/create?error=true";
        }

        return "redirect:/users/home";
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody UserDto userDto) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOptional.get();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmailId(userDto.getEmailId());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAge(userDto.getAge());
//        user.setDob(userDto.getDob());
        user.setPassword(userDto.getPassword());
        //user.setProfileImage(userDto.getProfileImage());

        //user.setProfileImage(sampleData);

        userRepository.save(user);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userRepository.delete(userOptional.get());
        return ResponseEntity.ok("User deleted successfully");
    }
}