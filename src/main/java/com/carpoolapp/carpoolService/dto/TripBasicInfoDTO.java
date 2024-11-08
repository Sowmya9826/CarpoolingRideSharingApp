package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Points;
import lombok.Data;

import java.util.Date;

@Data

public class TripBasicInfoDTO {
    private String userId;
    private String fullName;
    private String phoneNumber;
    private String profilePic;
    private String tripId;
    private String vehicleNumber;
    private Points pickupPoint;
    private Points destinationPoint;
    private Date tripStartTime;
    private int availableSeats;
    private int requestedSeats;
}
