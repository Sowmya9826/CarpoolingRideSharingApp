package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class RideParticipantDto {
    private Long rideId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String emailId;
    private byte[] profileImage;
    private RideParticipateRole role;

    private String profileImageBase64;

    public RideParticipantDto(Long rideId, String firstName, String lastName, String phoneNumber, String emailId, byte[] profileImage, RideParticipateRole role) {
        this.rideId = rideId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.emailId = emailId;
        this.profileImage = profileImage;
        this.role = role;
    }
}
