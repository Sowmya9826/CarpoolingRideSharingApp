package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.UserDto;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            UserDto userDto = new UserDto();
            userDto.setFirstName(user.get().getFirstName());

            return ResponseEntity.ok(userDto);
        }
    }

   /* @PostMapping("/{create}")
    public ResponseEntity<String> createUser(@ModelAttribute UserDto userDto,
                                             @RequestParam("file") MultipartFile file) {
        try {
            userService.saveUser(userDto, file);
            return ResponseEntity.status(HttpStatus.OK).body("User created successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save user data");
        }
    }*/

}
