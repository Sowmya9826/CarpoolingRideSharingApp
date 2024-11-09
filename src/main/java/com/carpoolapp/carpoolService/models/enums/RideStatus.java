package com.carpoolapp.carpoolService.models.enums;

public enum RideStatus {
    CREATED,      // The ride has been created but hasn't started
    ACTIVE,       // The ride is currently ongoing
    COMPLETED,    // The ride has been completed
    CANCELLED  // The ride was cancelled before completion
}


