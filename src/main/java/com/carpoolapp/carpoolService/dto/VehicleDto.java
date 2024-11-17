package com.carpoolapp.carpoolService.dto;


import com.carpoolapp.carpoolService.models.User;

import lombok.Data;

@Data
public class VehicleDto {


    private Long userId;
    private String number;
    private String type;
    private String name;
    private String color;
    private int seatCount;
}

