package com.carpoolapp.carpoolService.integrationTests;

import com.carpoolapp.carpoolService.models.Fare;
import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.models.RideParticipant;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.Vehicle;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import com.carpoolapp.carpoolService.models.enums.RideParticipantStatus;
import com.carpoolapp.carpoolService.repository.FareRepository;
import com.carpoolapp.carpoolService.repository.RideParticipantRepository;
import com.carpoolapp.carpoolService.repository.RideRepository;
import com.carpoolapp.carpoolService.repository.UserRepository;
import com.carpoolapp.carpoolService.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureMockMvc
public class RideControllerIntegrationTestWithServices {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private FareRepository fareRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RideParticipantRepository rideParticipantRepository;

    private Ride testRide;
    private User testUser;

    @BeforeEach
    public void setUp() {
        // Create and save a test user
        testUser = new User();
        testUser.setId(null); // Let the database generate the ID
        userRepository.save(testUser);

        // Create and save a test vehicle with the user as the owner
        Vehicle testVehicle = new Vehicle();
        testVehicle.setId(null); // Let the database generate the ID
        testVehicle.setOwner(testUser);
        vehicleRepository.save(testVehicle);

        // Create and save a test ride with the vehicle
        testRide = new Ride();
        testRide.setId(null); // Let the database generate the ID
        testRide.setVehicle(testVehicle);
        rideRepository.save(testRide);

        // Create and save a fare for the test ride
        Fare fare = new Fare();
        fare.setRide(testRide);
        fareRepository.save(fare);

        // Create and save a ride participant for the test ride
        RideParticipant rideParticipant = new RideParticipant();
        rideParticipant.setRide(testRide);
        rideParticipant.setParticipant(testUser);
        // Set other necessary fields
        rideParticipant.setRole(RideParticipateRole.PASSENGER); // Or DRIVER, depending on your test case
        rideParticipant.setStatus(RideParticipantStatus.ACTIVE); // Or appropriate status
        rideParticipant.setJoinedAt(LocalDate.now());
        rideParticipantRepository.save(rideParticipant);
    }

    @Test
    public void testShowRidesPage() throws Exception {
        /*
        Purpose: Verifies that the rides page is displayed correctly.
        Services/Repositories Involved:
                RideRepository
                RideParticipantRepository
                UserService
         */
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        mockMvc.perform(get("/rides/")
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void testShowCreateRidePage() throws Exception {
        /*
        Purpose: Verifies that the create ride page is displayed correctly.
        Services/Repositories Involved:
            VehicleRepository
         */
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        mockMvc.perform(get("/rides/create")
                        .session(session))
                .andExpect(status().isOk());
    }

    @Test
    public void testCancelRide() throws Exception {
        /*
        Purpose: Verifies that a ride can be cancelled successfully.
        Services/Repositories Involved:
            RideRepository
            RideParticipantService
            TransactionService
            FareService
         */
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        mockMvc.perform(post("/rides/" + testRide.getId() + "/cancel")
                        .session(session))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void testJoinRide() throws Exception {
        /*
        Purpose: Verifies that a user can join a ride successfully.
        Services/Repositories Involved:
            RideRepository
            RideParticipantService
            TransactionService
            UserRepository
         */
        MockHttpSession session = new MockHttpSession();
        session.setAttribute("userId", testUser.getId());

        mockMvc.perform(post("/rides/" + testRide.getId() + "/join")
                        .session(session))
                .andExpect(status().is3xxRedirection());
    }
}