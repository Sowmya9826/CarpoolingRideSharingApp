//package com.carpoolapp.carpoolService.repository;
//
//import com.carpoolapp.carpoolService.dto.RideParticipantDto;
//import com.carpoolapp.carpoolService.dto.UserRideInfoDto;
//import com.carpoolapp.carpoolService.models.Ride;
//import com.carpoolapp.carpoolService.models.RideParticipant;
//import com.carpoolapp.carpoolService.models.User;
//import com.carpoolapp.carpoolService.models.enums.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//@DataJpaTest
//class RideParticipantRepositoryTest {
//
//    @Autowired
//    private RideParticipantRepository rideParticipantRepository;
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    private User driver;
//    private User passenger;
//    private Ride upcomingRide;
//    private Ride pastRide;
//    private Ride recurringRide;
//    private RideParticipant activeParticipant;
//
//    @BeforeEach
//    void setUp() {
//        // Create test users
//        driver = createUser("John", "Driver", "1234567890", "john@example.com", "driver.jpg");
//        passenger = createUser("Jane", "Passenger", "0987654321", "jane@example.com", "passenger.jpg");
//
//        // Create test rides
//        LocalDate today = LocalDate.now();
//        LocalTime now = LocalTime.now();
//
//        upcomingRide = createRide(
//                today.plusDays(1),
//                now.plusHours(2),
//                now.plusHours(3),
//                "Start Point A",
//                "End Point A",
//                RideStatus.CREATED,
//                RideType.ONE_TIME,
//                "1,2,3,4,5"
//        );
//
//        pastRide = createRide(
//                today.minusDays(1),
//                now.minusHours(3),
//                now.minusHours(2),
//                "Start Point B",
//                "End Point B",
//                RideStatus.CREATED,
//                RideType.ONE_TIME,
//                null
//        );
//
//        recurringRide = createRide(
//                today,
//                now.plusHours(4),
//                now.plusHours(5),
//                "Start Point C",
//                "End Point C",
//                RideStatus.CREATED,
//                RideType.RECURRING,
//                "1,3,5"
//        );
//
//        // Create ride participants
//        activeParticipant = createRideParticipant(upcomingRide, passenger, RideParticipateRole.PASSENGER);
//        createRideParticipant(upcomingRide, driver, RideParticipateRole.DRIVER);
//        createRideParticipant(pastRide, passenger, RideParticipateRole.PASSENGER);
//        createRideParticipant(recurringRide, passenger, RideParticipateRole.PASSENGER);
//
//        entityManager.flush();
//    }
//
//    private User createUser(String firstName, String lastName, String phone, String email, String profileImage) {
//        User user = new User();
//        user.setFirstName(firstName);
//        user.setLastName(lastName);
//        user.setPhoneNumber(phone);
//        user.setEmailId(email);
////        user.setProfileImage(profileImage);
//        return entityManager.persist(user);
//    }
//
//    private Ride createRide(LocalDate date, LocalTime startTime, LocalTime endTime,
//                            String pickup, String destination, RideStatus status,
//                            RideType type, String daysOfWeek) {
//        Ride ride = new Ride();
//        ride.setDate(date);
//        ride.setStartTime(startTime);
//        ride.setEndTime(endTime);
////        ride.setPickupLocation(pickup);
////        ride.setDestinationLocation(destination);
//        ride.setStatus(status);
//        ride.setType(type);
//        ride.setDaysOfWeek(daysOfWeek);
//        return entityManager.persist(ride);
//    }
//
//    private RideParticipant createRideParticipant(Ride ride, User user, RideParticipateRole role) {
//        RideParticipant participant = new RideParticipant();
//        participant.setRide(ride);
//        participant.setParticipant(user);
//        participant.setRole(role);
//        participant.setStatus(RideParticipantStatus.ACTIVE);
//        return entityManager.persist(participant);
//    }
//
//    @Nested
//    @DisplayName("Find Rides Tests")
//    class FindRidesTests {
//
//        @Test
//        @DisplayName("Should find upcoming one-time rides for user")
//        void testFindUpcomingRidesForUser() {
//            List<UserRideInfoDto> rides = rideParticipantRepository.findUpcomingRidesForUser(
//                    passenger.getId(),
//                    LocalDate.now(),
//                    LocalTime.now(),
//                    RideParticipantStatus.ACTIVE
//            );
//
//            assertThat(rides)
//                    .isNotEmpty()
//                    .hasSize(1);
//
//            UserRideInfoDto ride = rides.get(0);
//            assertEquals(upcomingRide.getId(), ride.getRideId());
////            assertEquals(upcomingRide.getPickupLocation(), ride.getPickupLocation());
////            assertEquals(upcomingRide.getDestinationLocation(), ride.getDestinationLocation());
//        }
//
//        @Test
//        @DisplayName("Should find recurring rides for user")
//        void testFindRecurringRidesForUser() {
//            List<UserRideInfoDto> rides = rideParticipantRepository.findRecurringRidesForUser(
//                    passenger.getId(),
//                    RideParticipantStatus.ACTIVE
//            );
//
//            assertThat(rides)
//                    .isNotEmpty()
//                    .hasSize(1);
//
//            UserRideInfoDto ride = rides.get(0);
//            assertEquals(recurringRide.getId(), ride.getRideId());
//            assertEquals("1,3,5", ride.getDaysOfWeek());
//        }
//
//        @Test
//        @DisplayName("Should find past rides for user")
//        void testFindPastRidesForUser() {
//            List<UserRideInfoDto> rides = rideParticipantRepository.findPastRidesForUser(
//                    passenger.getId(),
//                    LocalDate.now(),
//                    LocalTime.now(),
//                    RideParticipantStatus.ACTIVE
//            );
//
//            assertThat(rides)
//                    .isNotEmpty()
//                    .hasSize(1);
//
//            UserRideInfoDto ride = rides.get(0);
//            assertEquals(pastRide.getId(), ride.getRideId());
//        }
//    }
//
//    @Nested
//    @DisplayName("Ride Participants Tests")
//    class RideParticipantsTests {
//
//        @Test
//        @DisplayName("Should find all participants by ride ID")
//        void testFindByRideId() {
//            List<RideParticipant> participants = rideParticipantRepository.findByRideId(upcomingRide.getId());
//
//            assertThat(participants)
//                    .isNotEmpty()
//                    .hasSize(2);
//
//            assertThat(participants)
//                    .extracting(RideParticipant::getRole)
//                    .containsExactlyInAnyOrder(RideParticipateRole.DRIVER, RideParticipateRole.PASSENGER);
//        }
//
//        @Test
//        @DisplayName("Should get active ride passengers details")
//        void testGetActiveRidePassengersDetails() {
//            List<RideParticipantDto> participants = rideParticipantRepository.getActiveRidePassengersDetails(upcomingRide.getId());
//
//            assertThat(participants)
//                    .isNotEmpty()
//                    .hasSize(2);
//
//            RideParticipantDto passengerDto = participants.stream()
//                    .filter(p -> p.getRole() == RideParticipateRole.PASSENGER)
//                    .findFirst()
//                    .orElseThrow();
//
//            assertEquals(passenger.getFirstName(), passengerDto.getFirstName());
//            assertEquals(passenger.getLastName(), passengerDto.getLastName());
//            assertEquals(passenger.getPhoneNumber(), passengerDto.getPhoneNumber());
//            assertEquals(passenger.getEmailId(), passengerDto.getEmailId());
//            assertEquals(passenger.getProfileImage(), passengerDto.getProfileImage());
//        }
//
//        @Test
//        @DisplayName("Should find ride participant by ride and user IDs")
//        void testFindByRideIdAndParticipantId() {
//            Optional<RideParticipant> found = rideParticipantRepository
//                    .findByRideIdAndParticipantId(upcomingRide.getId(), passenger.getId());
//
//            assertTrue(found.isPresent());
//            RideParticipant participant = found.get();
//            assertEquals(upcomingRide.getId(), participant.getRide().getId());
//            assertEquals(passenger.getId(), participant.getParticipant().getId());
//            assertEquals(RideParticipateRole.PASSENGER, participant.getRole());
//        }
//
//        @Test
//        @DisplayName("Should count active passengers in ride")
//        void testCountActivePassengersInRide() {
//            int count = rideParticipantRepository.countActivePassengersInRide(upcomingRide.getId());
//            assertEquals(1, count);
//        }
//    }
//
//    @Nested
//    @DisplayName("Edge Cases")
//    class EdgeCasesTests {
//
//        @Test
//        @DisplayName("Should return empty list for user with no rides")
//        void testNoRidesForUser() {
//            User newUser = createUser("New", "User", "1111111111", "new@example.com", "new.jpg");
//            entityManager.persist(newUser);
//
//            List<UserRideInfoDto> upcomingRides = rideParticipantRepository.findUpcomingRidesForUser(
//                    newUser.getId(),
//                    LocalDate.now(),
//                    LocalTime.now(),
//                    RideParticipantStatus.ACTIVE
//            );
//
//            assertThat(upcomingRides).isEmpty();
//        }
//
//        @Test
//        @DisplayName("Should handle non-existent ride participant")
//        void testNonExistentRideParticipant() {
//            Optional<RideParticipant> participant = rideParticipantRepository
//                    .findByRideIdAndParticipantId(999L, 999L);
//
//            assertTrue(participant.isEmpty());
//        }
//
//        @Test
//        @DisplayName("Should return empty list for cancelled ride")
//        void testCancelledRide() {
//            // Create a cancelled ride
//            Ride cancelledRide = createRide(
//                    LocalDate.now().plusDays(1),
//                    LocalTime.now().plusHours(1),
//                    LocalTime.now().plusHours(2),
//                    "Start",
//                    "End",
//                    RideStatus.CANCELLED,
//                    RideType.ONE_TIME,
//                    null
//            );
//            createRideParticipant(cancelledRide, passenger, RideParticipateRole.PASSENGER);
//            entityManager.flush();
//
//            List<UserRideInfoDto> rides = rideParticipantRepository.findUpcomingRidesForUser(
//                    passenger.getId(),
//                    LocalDate.now(),
//                    LocalTime.now(),
//                    RideParticipantStatus.ACTIVE
//            );
//
//            assertThat(rides)
//                    .extracting(UserRideInfoDto::getRideId)
//                    .doesNotContain(cancelledRide.getId());
//        }
//    }
////}