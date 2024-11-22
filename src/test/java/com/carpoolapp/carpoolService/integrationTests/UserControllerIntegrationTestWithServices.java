//package com.carpoolapp.carpoolService.integrationTests;
//
//import com.carpoolapp.carpoolService.controller.UserController;
//import com.carpoolapp.carpoolService.models.User;
//import com.carpoolapp.carpoolService.repository.UserRepository;
//import com.carpoolapp.carpoolService.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerIntegrationTestWithServices {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private UserController userController;
//
//    private User testUser;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Create and save a test user
//        testUser = new User();
//        testUser.setId(1L);
//        testUser.setFirstName("Test");
//        testUser.setLastName("User");
//        testUser.setEmailId("testuser@example.com");
//        testUser.setPhoneNumber("1234567890");
//        testUser.setPassword("password");
//        // Convert the date string to a Date object
//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            testUser.setDob(dateFormat.parse("1998-01-01"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        when(userRepository.save(any(User.class))).thenReturn(testUser);
//        when(userRepository.findById(1L)).thenReturn(java.util.Optional.of(testUser));
//    }
//
//    @Test
//    public void testCreateUser() throws Exception {
//        String userJson = "{\"firstName\":\"New\",\"lastName\":\"User\",\"emailId\":\"newuser@example.com\",\"phoneNumber\":\"0987654321\",\"password\":\"newpassword\"}";
//
//        mockMvc.perform(post("/users/create")
//                        .contentType("application/json")
//                        .content(userJson))
//                .andExpect(status().is3xxRedirection());
//    }
//
//    @Test
//    public void testGetUserProfile() throws Exception {
//        mockMvc.perform(get("/users/profile")
//                        .sessionAttr("userId", testUser.getId()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("Test"))
//                .andExpect(jsonPath("$.lastName").value("User"))
//                .andExpect(jsonPath("$.emailId").value("testuser@example.com"))
//                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));
//    }
//
//    @Test
//    public void testUpdateUserProfile() throws Exception {
////        String updatedUserJson = "{\"firstName\":\"Updated\",\"lastName\":\"User\",\"emailId\":\"updateduser@example.com\",\"phoneNumber\":\"0987654321\",\"password\":\"updatedpassword\"}";
//        String updatedUserJson = "{\"firstName\":\"Updated\",\"lastName\":\"User\",\"emailId\":\"updateduser@example.com\",\"phoneNumber\":\"0987654321\",\"password\":\"updatedpassword\",\"dob\":\"1998-01-01\"}";
//        mockMvc.perform(post("/users/update")
//                        .sessionAttr("userId", testUser.getId())
//                        .contentType("application/json")
//                        .content(updatedUserJson))
//                .andExpect(status().is3xxRedirection());
//    }
//}