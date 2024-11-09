package com.carpoolapp.carpoolService.controller;

import com.carpoolapp.carpoolService.dto.UserInfoDTO;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public UserInfoDTO getUserById(@PathVariable String id) {
        Optional<User> user = userRepository.findById(id);
        UserInfoDTO userInfo = new UserInfoDTO();




        return userInfo;
    }
}
