package com.carpoolapp.carpoolService.dto;

import lombok.Data;

@Data
public class VehicleTypeDTO {
    private String vehicleType;
    public VehicleTypeDTO(String vehicleType) {
        this.vehicleType = vehicleType;
    }
}
