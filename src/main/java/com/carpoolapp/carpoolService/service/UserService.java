package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.dto.RideParticipantDto;
import com.carpoolapp.carpoolService.dto.UserDto;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.respository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(UserDto userDto, MultipartFile file) throws IOException {
        User user = new User();
        setUserData(user, userDto);
        setUserProfileImage(user, file);
        userRepository.save(user);

        return user;
    }

    public void updateUser(User user, UserDto userDto, MultipartFile file) {
        setUserData(user, userDto);
        setUserProfileImage(user, file);
        userRepository.save(user);
    }

    public void initializeUserSession(HttpSession session, User user) {
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getFirstName());
    }

    public void addProfileImageToUIModel(User user, Model model) {
        // Convert profile image to Base64 string
        String profileImage = null;
        if (user.getProfileImage() != null) {
            profileImage = Base64.getEncoder().encodeToString(user.getProfileImage());
        }

        model.addAttribute("profileImage", profileImage);
    }

    // convert profile images in the list of rideParticipantDto to base64 string
    public void addProfileImagesToRideParticipantDto(List<RideParticipantDto> rideParticipantDtos) {
        for (RideParticipantDto rideParticipantDto : rideParticipantDtos) {
            byte[] profileImage = rideParticipantDto.getProfileImage();
            if (profileImage != null) {
                rideParticipantDto.setProfileImageBase64(Base64.getEncoder().encodeToString(profileImage));
            }
        }
    }

    private void setUserData(User user, UserDto userDto) {
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmailId(userDto.getEmailId());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setPassword(userDto.getPassword());
        setUserDob(user, userDto);
    }

    private void setUserDob(User user, UserDto userDto) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dob = dateFormat.parse(userDto.getDob());
            user.setDob(dob);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected yyyy-MM-dd", e);
        }
    }

    private void setUserProfileImage(User user, MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            try {
                user.setProfileImage(file.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Failed to read profile image", e);
            }
        }
    }
}
