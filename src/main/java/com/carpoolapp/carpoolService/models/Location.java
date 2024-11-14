package com.carpoolapp.carpoolService.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Location {
    @Id
    private String id;
    private double latitude;
    private double longitude;
    private String address;
}
