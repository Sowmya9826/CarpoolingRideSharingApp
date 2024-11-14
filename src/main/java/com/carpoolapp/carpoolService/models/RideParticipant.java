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

    public User getParticipant() {
        return participant;
    }

    public void setParticipant(User participant) {
        this.participant = participant;
    }

    public RideParticipateRole getRole() {
        return role;
    }

    public void setRole(RideParticipateRole role) {
        this.role = role;
    }
}



