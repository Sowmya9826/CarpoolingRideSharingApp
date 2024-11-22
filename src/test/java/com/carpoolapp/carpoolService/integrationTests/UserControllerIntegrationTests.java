package com.carpoolapp.carpoolService.integrationTests;

import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    private User testUser;

    @BeforeEach
    public void setUp() {
        // Create and save a test user
        testUser = new User();
        testUser.setId(null); // Let the database generate the ID
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmailId("testuser@example.com");
        testUser.setPhoneNumber("1234567890");
        testUser.setPassword("password");
        // Set a valid date of birth
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            testUser.setDob(dateFormat.parse("1990-01-01"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userRepository.save(testUser);
    }

    @Test
    public void testGetUserDetails() throws Exception {
        mockMvc.perform(get("/users/" + testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"))
                .andExpect(jsonPath("$.emailId").value("testuser@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("1234567890"));
    }


}