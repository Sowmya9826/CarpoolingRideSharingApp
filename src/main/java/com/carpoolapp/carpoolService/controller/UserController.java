package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.UserDto;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
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
            userDto.setDob(user.get().getDob());
            userDto.setPassword(user.get().getPassword());
            //userDto.setProfileImage(user.get().getProfileImage());
            return ResponseEntity.ok(userDto);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmailId(userDto.getEmailId());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setAge(userDto.getAge());
        user.setDob(userDto.getDob());
        user.setPassword(userDto.getPassword());
        //user.setProfileImage(userDto.getProfileImage());

        //byte[] sampleData = {1, 2, 3, 4, 5};
        //user.setProfileImage(sampleData);

        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
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
        user.setDob(userDto.getDob());
        user.setPassword(userDto.getPassword());
        //user.setProfileImage(userDto.getProfileImage());

        //user.setProfileImage(sampleData);

        userRepository.save(user);
        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userRepository.delete(userOptional.get());
        return ResponseEntity.ok("User deleted successfully");
    }
}