package com.carpoolapp.carpoolService.models.enums;

public enum RideParticipantStatus {
    ACTIVE,     // One-time rides: Either the participant is still part of the ride and the ride is yet to happen;
                // Or, the participant was part of the ride until the ride was completed
                // Recurring rides: The participant is still part of the ride

    CANCELLED,  // Either the participant cancelled or the ride owner cancelled the ride itself
}
