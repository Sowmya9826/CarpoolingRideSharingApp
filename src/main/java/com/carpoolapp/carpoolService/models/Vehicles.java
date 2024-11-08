package com.carpoolapp.carpoolService.models;

import lombok.Data;

@Data
public class Vehicles {
    private String userId;
    private String vehicleNumber;
    private String vehicleType;
    private String vehicleName;
    private String vehicleColor;
}
