package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Point;
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
    private Point pickupPoint;
    private Point destinationPoint;
    private Date tripStartTime;
    private int availableSeats;
    private int requestedSeats;
}
