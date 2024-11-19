package com.carpoolapp.carpoolService.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RideDto {

    private Long vehicleId;
    private double startLatitude;
    private double startLongitude;
    private String startAddress;
    private double endLatitude;
    private double endLongitude;
    private String endAddress;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isRecurring;
    private String daysOfWeek;
    private LocalDate date;
    private LocalDate createdDate;
}
