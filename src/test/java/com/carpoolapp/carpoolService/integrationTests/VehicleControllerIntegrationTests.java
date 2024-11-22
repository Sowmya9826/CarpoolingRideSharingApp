package com.carpoolapp.carpoolService.integrationTests;

import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.Vehicle;
import com.carpoolapp.carpoolService.repository.UserRepository;
import com.carpoolapp.carpoolService.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    private Vehicle testVehicle;
    private User testUser;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        // Create and save a test user
        testUser = new User();
        setField(testUser, "firstName", "Test");
        setField(testUser, "lastName", "User");
        setField(testUser, "emailId", "testuser@example.com");
        setField(testUser, "phoneNumber", "1234567890");
        setField(testUser, "password", "password");
        userRepository.save(testUser);

        // Create and save a test vehicle
        testVehicle = new Vehicle();
        setField(testVehicle, "owner", testUser);
        setField(testVehicle, "number", "ABC123");
        setField(testVehicle, "type", "Sedan");
        setField(testVehicle, "name", "Toyota Camry");
        setField(testVehicle, "color", "Blue");
        setField(testVehicle, "seatCount", 5);
        vehicleRepository.save(testVehicle);
    }

    private void setField(Object object, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }


    @Test
    public void testShowVehicles() throws Exception {
        mockMvc.perform(get("/vehicles/")
                        .sessionAttr("userId", testUser.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("vehicles"));
    }
}