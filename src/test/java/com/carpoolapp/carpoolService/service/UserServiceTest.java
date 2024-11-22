package com.carpoolapp.carpoolService.service;

import com.carpoolapp.carpoolService.dto.UserDto;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private Model model;

    @InjectMocks
    private UserService userService;

    private UserDto userDto;
    private User user;
    private MultipartFile multipartFile;
    private byte[] imageBytes;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setFirstName("John");
        userDto.setLastName("Doe");
        userDto.setEmailId("john.doe@example.com");
        userDto.setPhoneNumber("1234567890");
        userDto.setPassword("password123");
        userDto.setDob("1990-01-01");

        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");

        imageBytes = "test image content".getBytes();
        multipartFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                imageBytes
        );
    }

    @Test
    void registerUser_Success() throws IOException {
        // Arrange
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        User registeredUser = userService.registerUser(userDto, multipartFile);

        // Assert
        verify(userRepository).save(any(User.class));
        assertEquals(userDto.getFirstName(), registeredUser.getFirstName());
        assertEquals(userDto.getLastName(), registeredUser.getLastName());
        assertArrayEquals(imageBytes, registeredUser.getProfileImage());
    }

    @Test
    void registerUser_WithoutProfileImage() throws IOException {
        // Act
        User registeredUser = userService.registerUser(userDto, null);

        // Assert
        verify(userRepository).save(any(User.class));
        assertNull(registeredUser.getProfileImage());
    }

    @Test
    void registerUser_InvalidDateFormat() {
        // Arrange
        userDto.setDob("invalid-date");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(userDto, multipartFile));
    }

    @Test
    void updateUser_Success() throws IOException {
        // Act
        userService.updateUser(user, userDto, multipartFile);

        // Assert
        verify(userRepository).save(user);
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertArrayEquals(imageBytes, user.getProfileImage());
    }

    @Test
    void updateUser_WithoutProfileImage() {
        // Act
        userService.updateUser(user, userDto, null);

        // Assert
        verify(userRepository).save(user);
        assertNull(user.getProfileImage());
    }

    @Test
    void initializeUserSession_Success() {
        // Arrange
        HttpSession session = new MockHttpSession();

        // Act
        userService.initializeUserSession(session, user);

        // Assert
        assertEquals(user.getId(), session.getAttribute("userId"));
        assertEquals(user.getFirstName(), session.getAttribute("userName"));
    }

    @Test
    void addProfileImageToUIModel_WithImage() {
        // Arrange
        user.setProfileImage(imageBytes);

        // Act
        userService.addProfileImageToUIModel(user, model);

        // Assert
        verify(model).addAttribute(eq("profileImage"),
                eq(Base64.getEncoder().encodeToString(imageBytes)));
    }

    @Test
    void addProfileImageToUIModel_WithoutImage() {
        // Arrange
        user.setProfileImage(null);

        // Act
        userService.addProfileImageToUIModel(user, model);

        // Assert
        verify(model).addAttribute("profileImage", null);
    }

//    @Test
//    void addProfileImagesToRideParticipantDto_Success() {
//        // Arrange
//        RideParticipantDto dto1 = new RideParticipantDto();
//        dto1.setProfileImage(imageBytes);
//
//        RideParticipantDto dto2 = new RideParticipantDto();
//        dto2.setProfileImage(null);
//
//        List<RideParticipantDto> dtos = Arrays.asList(dto1, dto2);
//
//        // Act
//        userService.addProfileImagesToRideParticipantDto(dtos);
//
//        // Assert
//        assertEquals(Base64.getEncoder().encodeToString(imageBytes),
//                dto1.getProfileImageBase64());
//        assertNull(dto2.getProfileImageBase64());
//    }

    @Test
    void setUserData_ValidDate() {
        // Arrange
        User user = new User();

        // Act
        userService.updateUser(user, userDto, null);

        // Assert
        assertEquals(userDto.getFirstName(), user.getFirstName());
        assertEquals(userDto.getLastName(), user.getLastName());
        assertEquals(userDto.getEmailId(), user.getEmailId());
        assertEquals(userDto.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(userDto.getPassword(), user.getPassword());
        assertNotNull(user.getDob());
    }

    @Test
    void setUserData_InvalidDate() {
        // Arrange
        userDto.setDob("invalid-date");
        User user = new User();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(user, userDto, null));
    }

   // @Test
//    void setUserProfileImage_IOExceptionHandling() {
//        // Arrange
//        MultipartFile badFile = mock(MultipartFile.class);
//        when(badFile.isEmpty()).thenReturn(false);
//       // when(badFile.getBytes()).thenThrow(new IOException("Test IO Exception"));
//
//        // Act & Assert
//        assertThrows(RuntimeException.class,
//                () -> userService.updateUser(user, userDto, badFile));
//    }
}