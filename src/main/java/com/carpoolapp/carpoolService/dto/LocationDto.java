package com.carpoolapp.carpoolService.dto;

import lombok.Data;

@Data
public class LocationDto {
    private double latitude;
    private double longitude;
    private String address;
}
