package com.carpoolapp.carpoolService.models;

import com.carpoolapp.carpoolService.models.enums.RideParticipantStatus;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class RideParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rideId", foreignKey = @ForeignKey(name = "fk_ride"))
    private Ride ride;

    @ManyToOne
    @JoinColumn(name = "userId", foreignKey = @ForeignKey(name = "fk_ride_user"))
    private User participant;

    private RideParticipateRole role;

    @Enumerated(EnumType.STRING)
    private RideParticipantStatus status;

    private Date cancelledAt;
}



