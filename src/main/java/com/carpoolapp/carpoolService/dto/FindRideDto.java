package com.carpoolapp.carpoolService.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class FindRideDto {
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private LocalDate date;
    private LocalTime endTime;
    private boolean isRecurring;
}
