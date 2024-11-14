package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.FareStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Fare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "rideId", foreignKey = @ForeignKey(name = "fk_ride_fare"))
    private Ride ride;
    private double amount;
    private FareStatus status;
}
