package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RideParticipantDtoTest {

    private RideParticipantDto participantDto;
    private byte[] profileImage;
    private final Long rideId = 1L;
    private final String firstName = "John";
    private final String lastName = "Doe";
    private final String phoneNumber = "1234567890";
    private final String emailId = "john.doe@example.com";
    private final RideParticipateRole role = RideParticipateRole.DRIVER;

    @BeforeEach
    void setUp() {
        profileImage = new byte[] { 1, 2, 3, 4, 5 };  // Sample image byte array
        participantDto = new RideParticipantDto(rideId, firstName, lastName, phoneNumber, emailId, profileImage, role);
    }

    @Test
    void testGettersAndSetters() {
        // Verifying getters work as expected
        assertEquals(rideId, participantDto.getRideId());
        assertEquals(firstName, participantDto.getFirstName());
        assertEquals(lastName, participantDto.getLastName());
        assertEquals(phoneNumber, participantDto.getPhoneNumber());
        assertEquals(emailId, participantDto.getEmailId());
        assertArrayEquals(profileImage, participantDto.getProfileImage());
        assertEquals(role, participantDto.getRole());

        // Verifying setters by changing values
        participantDto.setRideId(2L);
        participantDto.setFirstName("Jane");
        participantDto.setLastName("Smith");
        participantDto.setPhoneNumber("0987654321");
        participantDto.setEmailId("jane.smith@example.com");
        participantDto.setProfileImage(new byte[] { 6, 7, 8, 9, 10 });
        participantDto.setRole(RideParticipateRole.PASSENGER);

        assertEquals(2L, participantDto.getRideId());
        assertEquals("Jane", participantDto.getFirstName());
        assertEquals("Smith", participantDto.getLastName());
        assertEquals("0987654321", participantDto.getPhoneNumber());
        assertEquals("jane.smith@example.com", participantDto.getEmailId());
        assertArrayEquals(new byte[] { 6, 7, 8, 9, 10 }, participantDto.getProfileImage());
        assertEquals(RideParticipateRole.PASSENGER, participantDto.getRole());
    }

    @Test
    void testConstructor() {
        // Ensure the constructor properly initializes the fields
        assertNotNull(participantDto);
        assertEquals(rideId, participantDto.getRideId());
        assertEquals(firstName, participantDto.getFirstName());
        assertEquals(lastName, participantDto.getLastName());
        assertEquals(phoneNumber, participantDto.getPhoneNumber());
        assertEquals(emailId, participantDto.getEmailId());
        assertArrayEquals(profileImage, participantDto.getProfileImage());
        assertEquals(role, participantDto.getRole());
    }

    @Test
    void testEqualsAndHashCode() {
        // Create another participantDto with the same values
        RideParticipantDto participant2 = new RideParticipantDto(rideId, firstName, lastName, phoneNumber, emailId, profileImage, role);

        // Test equality
        assertEquals(participantDto, participant2);
        assertEquals(participantDto.hashCode(), participant2.hashCode());

        // Modify participant2 and test inequality
        participant2.setPhoneNumber("1122334455");
        assertNotEquals(participantDto, participant2);
    }

//    @Test
//    void testToString() {
//        String expectedString = "RideParticipantDto(rideId=1, firstName=John, lastName=Doe, phoneNumber=1234567890, " +
//                "emailId=john.doe@example.com, profileImage=" + participantDto.getProfileImage().hashCode() + ", role=DRIVER)";
//        // Use the actual hashcode in the expected string
//        assertTrue(participantDto.toString().contains("profileImage=" + participantDto.getProfileImage().hashCode()));
//    }


    @Test
    void testProfileImageBase64Handling() {
        // Setting and getting profileImageBase64 value
        String base64String = "base64EncodedImageString==";
        participantDto.setProfileImageBase64(base64String);
        assertEquals(base64String, participantDto.getProfileImageBase64());
    }
}
