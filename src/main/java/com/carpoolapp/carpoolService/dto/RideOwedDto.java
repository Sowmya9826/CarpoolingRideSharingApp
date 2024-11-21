package com.carpoolapp.carpoolService.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RideOwedDto {
    private Long id;
    private String startLocation;
    private String endLocation;
    private LocalDate date;
    private String driverFirstName;
    private String driverLastName;
    private String driverPhoneNumber;
    private double amountOwed;

    public RideOwedDto(Long id, String address, String address1, LocalDate date, String firstName, String lastName, String phoneNumber, double amount) {
        this.id = id;
        this.startLocation = address;
        this.endLocation = address1;
        this.date = date;
        this.driverFirstName = firstName;
        this.driverLastName = lastName;
        this.driverPhoneNumber = phoneNumber;
        this.amountOwed = amount;
    }
}
