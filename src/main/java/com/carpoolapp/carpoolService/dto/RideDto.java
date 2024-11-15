package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Location;
import com.carpoolapp.carpoolService.models.Vehicle;
import com.carpoolapp.carpoolService.models.enums.RideStatus;
import lombok.Data;

import java.util.Date;
@Data
public class RideDto {

    private Vehicle vehicle;
    private Location pickupLocation;
    private Location destinationLocation;
    private RideStatus status;
    private Date startTime;
    private Date endTime;
    private int availableSeats;
    private Date createdDate;
}
