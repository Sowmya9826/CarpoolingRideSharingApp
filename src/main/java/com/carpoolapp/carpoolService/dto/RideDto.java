package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Location;
import com.carpoolapp.carpoolService.models.Vehicle;
import com.carpoolapp.carpoolService.models.enums.RideStatus;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;
@Data
public class RideDto {

    private Long vehicleId;
    private double pickupLatitude;
    private double pickupLongitude;
    private double endLatitude;
    private double endLongitude;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isRecurring;
    private String daysOfWeek;
    private Date date;
    private Date createdDate;
}
