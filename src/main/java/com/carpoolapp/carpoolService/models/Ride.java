package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.RideStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;


@Entity
@Data
public class Ride {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne
    @JoinColumn(name = "vehicleId", foreignKey = @ForeignKey(name = "fk_ride_vehicle"))
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "pickupLocationId")
    private Location pickupLocation;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @ManyToOne
    @JoinColumn(name = "destinationLocationId")
    private Location destinationLocation;
    private Date startTime;
    private Date endTime;
    private int availableSeats;
    private Date createdDate = new Date();
}
