package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.FareStatus;
import jakarta.persistence.*;
import lombok.Data;

@Data
public class Fare {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "rideId", foreignKey = @ForeignKey(name = "fk_ride_fare"))
    private Ride ride;
    private double amount;
    private FareStatus status;
}
