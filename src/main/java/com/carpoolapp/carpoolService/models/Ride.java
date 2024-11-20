package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.RideStatus;
import com.carpoolapp.carpoolService.models.enums.RideType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Data
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicleId", foreignKey = @ForeignKey(name = "fk_ride_vehicle"))
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "pickupLocationId")
    private Location pickupLocation;

    @Enumerated(EnumType.STRING)
    private RideStatus status;

    @Enumerated(EnumType.STRING)
    private RideType type;

    @ManyToOne
    @JoinColumn(name = "destinationLocationId")
    private Location destinationLocation;

    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private String daysOfWeek;
    private int availableSeats;
    private LocalDate createdDate;
}
