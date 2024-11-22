package com.carpoolapp.carpoolService.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class RecurringToOneTimeRideLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recurringRideId", foreignKey = @ForeignKey(name = "fk_recurring_ride"))
    private Ride recurringRide;

    @OneToOne
    @JoinColumn(name = "oneTimeRideId", foreignKey = @ForeignKey(name = "fk_one_time_ride"))
    private Ride oneTimeRide;
}
