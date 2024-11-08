package com.carpoolapp.carpoolService.models;

import lombok.Data;

import java.util.Date;

@Data
public class Riders {
    private String rideId;
    private String userId;
    private String allottedTripId;
    private String rideStatus;
    private Points pickupPoint;
    private Points destinationPoint;
    private Date rideStartTime;
    private Date rideEndTime;
    private double rideDistance;
    private int requestedSeats = 1;
    private Date createdDate = new Date();
    private String createdBy;
    private boolean isRiderTripOwner;
}
