package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.models.enums.FareStatus;
import lombok.Data;

@Data
public class FareDto {
    private Ride ride;
    private double amount;
    private FareStatus status;
}
