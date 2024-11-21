package com.carpoolapp.carpoolService.models;

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
}
