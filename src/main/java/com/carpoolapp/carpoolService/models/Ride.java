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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Location getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(Location pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public RideStatus getStatus() {
        return status;
    }

    public void setStatus(RideStatus status) {
        this.status = status;
    }

    public Location getDestinationLocation() {
        return destinationLocation;
    }

    public void setDestinationLocation(Location destinationLocation) {
        this.destinationLocation = destinationLocation;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
