package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.FareStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Fare {
    @Id
    private String id;
    @OneToOne
    @JoinColumn(name = "rideId", foreignKey = @ForeignKey(name = "fk_ride_fare"))
    private Ride ride;
    private double amount;
    private FareStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public FareStatus getStatus() {
        return status;
    }

    public void setStatus(FareStatus status) {
        this.status = status;
    }
}
