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

    private Point pickupPoint;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    private Point destinationPoint;
    private Date startTime;
    private Date endTime;
    private int availableSeats;
    private Date createdDate = new Date();
}
