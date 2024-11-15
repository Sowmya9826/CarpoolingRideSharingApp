package com.carpoolapp.carpoolService.dto;

import com.carpoolapp.carpoolService.models.Ride;
import com.carpoolapp.carpoolService.models.User;
import com.carpoolapp.carpoolService.models.enums.RideParticipateRole;

import lombok.Data;

@Data

public class RideParticipantDto {
    private Ride ride;
    private User participant;
    private RideParticipateRole role;
}
