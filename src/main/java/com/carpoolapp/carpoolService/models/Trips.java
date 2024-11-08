package com.carpoolapp.carpoolService.models;
import lombok.Data;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.List;
@Data
public class Trips {
    private String tripId;
    private String tripStatus;
    private String userId;
    private String vehicleNumber;
    private Points pickupPoint;
    private Points destinationPoint;
    private Date tripStartTime;
    private Date tripEndTime;
    private int offeredSeats;
    private int currSeats;
    private double pricePerKm;
    private List<ObjectId> joinedRidersId;
    private Date createdDate = new Date();
    private String createdBy;
}
