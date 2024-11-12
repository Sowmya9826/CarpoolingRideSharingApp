package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Location;
import lombok.Data;

import java.util.Date;
@Data
public class RideDTO {
    // userId of the Trip Owner
    private String userId;
    private String tripId;
    private Location pickupPoint;
    private Location destinationPoint;
    private Date rideStartTime;
    private int requestedSeats;
    private String tripStatus;
    private String rideStatus;
}
