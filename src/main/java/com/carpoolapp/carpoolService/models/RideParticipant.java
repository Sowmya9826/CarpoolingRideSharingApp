package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RideParticipant {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "rideId", foreignKey = @ForeignKey(name = "fk_ride"))
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "fk_ride_user"))
    private User participant;
    private RideParticipateRole role;
}



